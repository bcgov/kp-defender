package gov.ca.bc.qp.QPDefender.web;

import gov.ca.bc.qp.QPDefender.config.MyResolver;
import gov.ca.bc.qp.QPDefender.config.MyRoles;
import gov.ca.bc.qp.qpcommon.authenticate.DAOUser;
import gov.ca.bc.qp.qpcommon.authenticate.QPPrincipal;
import gov.ca.bc.qp.qpcommon.authenticate.User;
import gov.ca.bc.qp.qpcommon.authenticate.UserCredentials;
import gov.ca.bc.qp.qpcommon.code.ObjectNotFoundException;
import gov.ca.bc.qp.qpcommon.connection.DAOException;
import gov.ca.bc.qp.qpcommon.dom.XSLTTransformer;
import gov.ca.bc.qp.qpcommon.marshal.QPMarshaller;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.ServletContext;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

/**
 * JAX-RS interface for accessing a users own information.
 * @author spencer.tickner
 */
@Path("{xsl:.+}/user")
public class WebUser {

	Logger log = Logger.getLogger(getClass());

	// Grab our context to get our principal.
	@Context private SecurityContext securityContext;
	@Context private HttpHeaders header;
	@Context private UriInfo uriInfo;
	
	
	QPPrincipal principal = null;
	/**
	 * Helper method for ensuring we don't get null pointers if running outside a 
	 * 	security context.
	 * @return	A object representing the user accessing this interface.
	 */
	public QPPrincipal getPrincipal() {
		if(securityContext != null)
			principal = (QPPrincipal)securityContext.getUserPrincipal();
		return principal;
	}
	
	/**
	 * This is for testing. Usually the container's securityContext takes care of setting the principal.
	 * @param principal Information on the user that's logged in and accessing the service.
	 */
	protected void setPrincipal(QPPrincipal principal) {
		this.principal = principal;
	}
	
	// Get our xslt path for transformations.
	@PathParam("xsl") public String xsl_global;
	
	/**
	 * Allow anyone (that is logged in and belongs to one of the roles defined in 
	 * <strong>this</strong> web.xml).
	 * @return The credentials for the currently logged in user.
	 */
	@GET
	@Path("/me")
	@Produces({MediaType.TEXT_HTML, MediaType.APPLICATION_XML})
	@PermitAll
	public Response getMyInformation() {
		MediaType type = MediaType.APPLICATION_XML_TYPE;
		Response response = null;
		int userId = this.getPrincipal().getUserId();
		DAOUser dao = new DAOUser();
		UserCredentials creds = null;
		try {
			creds = dao.LookupUserCredentialsByUserId(userId);
			creds.makeSecure();
			QPMarshaller marshaller = new QPMarshaller();
			Document doc = marshaller.marshalToDom(creds);
			// Transform our xsl
			if(!xsl_global.equals(MyResolver.NO_TRANSFORM)) {
				MyResolver resolver = new MyResolver(this.xsl_global, this.getPrincipal(), this.uriInfo);
				XSLTTransformer trans = XSLTTransformer.getInstance(resolver);
				doc = trans.transform(doc, resolver.getParams());
				type = MediaType.TEXT_HTML_TYPE;
			}
			response = Response.ok().entity(doc).build();
		} catch (ObjectNotFoundException e) {
			log.warn("User Credentials where not found", e);
			response = Response.status(Status.NOT_FOUND).build();
		} catch (DAOException e) {
			log.error("Error occurred while accessing our Data source for User Credentials", e);
			response = Response.serverError().build();
		} catch (JAXBException e) {
			log.error("Error occurred while rendering User Credentials", e);
			response = Response.serverError().build();
		} catch (ParserConfigurationException e) {
			log.error("Error occurred while rendering User Credentials", e);
			response = Response.serverError().build();
		} catch (TransformerException e) {
			log.error("Error occurred while transforming User Credentials", e);
			response = Response.serverError().build();
		}
		
		return response;
	}
	
	// The minimum length a password may be.
	private static final int minCredLength = 4;
	
	/**
	 * This updates the currently logged in users credentials.
	 * @param newCredential		The new credential to be updated.
	 * @param repeatCredential	A repeat of the new credential above to ensure it's valid.
	 * @param newCredential2	The second new credential, only for SERVER_IP authentication.
	 * @param repeatCredential2	A repeat of the second new credential above to ensure it's valid.
	 * @return
	 */
	@POST
	@Produces({MediaType.TEXT_HTML, MediaType.APPLICATION_XML})
	@PermitAll
	public Response updateCredentials(
			@FormParam("newCredential1") String newCredential,
			@FormParam("repeatCredential1") String repeatCredential,
			@FormParam("newCredential2") String newCredential2,
			@FormParam("repeatCredential2") String repeatCredential2) {
		MediaType type = MediaType.APPLICATION_XML_TYPE;
		Response response = null;
		Document doc = null;
		UserCredentials creds = null;
		
		// remove null pointers
		if(newCredential == null)
			newCredential = "";
		if(repeatCredential == null)
			repeatCredential = "";
		if(newCredential2 == null)
			newCredential2 = "";
		if(repeatCredential2 == null)
			repeatCredential2 = "";
		
		try {
			if(!newCredential.equals(repeatCredential) || newCredential2.equals(repeatCredential2)) {
				doc = Message.UNMATCHED_CREDENTIALS.getMessage();
			} else if(newCredential.length() < minCredLength) {
				doc = Message.CREDENTIALS_TO_SHORT.getMessage();
			} else {
				DAOUser dao = new DAOUser();
				
				try {
					creds = dao.LookupUserCredentialsByUserId(this.getPrincipal().getUserId());
					creds.updateCredentials(newCredential, newCredential2);
					dao.updateUserCredentials(creds);
					doc = Message.SUCCESS.getMessage();
				} catch (ObjectNotFoundException e) {
					// Usually we would return a 404, but we want the user to know that there credentials may not have been changed.
					doc = Message.USER_NOT_FOUND.getMessage();
				}

			}
			
			// Now resolve the document based on passed in stylesheet.
			if(!xsl_global.equals(MyResolver.NO_TRANSFORM)) {
				MyResolver resolver = new MyResolver(this.xsl_global, this.getPrincipal(), this.uriInfo);
				XSLTTransformer trans = XSLTTransformer.getInstance(resolver);
				doc = trans.transform(doc, resolver.getParams());
				type = MediaType.TEXT_HTML_TYPE;
			}
			response = Response.ok().entity(doc).type(type).build();
		} catch (DAOException e) {
			log.error("DAO Exception occurred when updating user credentials", e);
			response = Response.serverError().build();
		} catch (TransformerException e) {
			log.error("Transformer Exception occurred when updating user credentials", e);
			response = Response.serverError().build();
		} catch (ParserConfigurationException e) {
			log.error("ParserConfigurationException Exception occurred when updating user credentials", e);
			response = Response.serverError().build();
		} finally {}
		
		return response;
			
	}
	
	
}
