package gov.ca.bc.qp.QPDefender.web;

import java.io.InputStream;
import java.util.List;

import gov.ca.bc.qp.QPDefender.config.MyResolver;
import gov.ca.bc.qp.QPDefender.utility.QPMediaType;
import gov.ca.bc.qp.qpcommon.authenticate.Product;
import gov.ca.bc.qp.qpcommon.authenticate.QPPrincipal;
import gov.ca.bc.qp.qpcommon.dom.XSLTTransformer;
import gov.ca.bc.qp.qpcommon.marshal.QPMarshaller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

@Path("{xsl:.+}/resources")
public class WebResources {

	Logger log = Logger.getLogger(getClass());

	// Grab our context to get our principal.
	@Context private SecurityContext securityContext;
	@Context private HttpHeaders header;
	@Context private UriInfo uriInfo;
	
	/**
	 * Helper method for ensuring we don't get null pointers if running outside a 
	 * 	security context.
	 * @return	A object representing the user accessing this interface.
	 */
	public QPPrincipal getPrincipal() {
		QPPrincipal principal = null;
		if(securityContext != null)
			principal = (QPPrincipal)securityContext.getUserPrincipal();
		return principal;
	}
	
	// Get our xslt path for transformations.
	@PathParam("xsl") public String xsl_global;
	
	
	@GET
	@Path("/media/{path}")
	public Response getResource(@PathParam("path") String path) {
		Response response = null;
		MediaType type = QPMediaType.getMediaTypeFromPath(path);
		path = "/resources/media/" + path;
		InputStream resource = this.getClass().getResourceAsStream(path);
			// Transform our xsl
		if(!xsl_global.equals(MyResolver.NO_TRANSFORM) && type == MediaType.TEXT_XML_TYPE) {
			MyResolver resolver = new MyResolver(this.xsl_global, this.getPrincipal(), this.uriInfo);
			Document document = null;
			try {
				XSLTTransformer trans = XSLTTransformer.getInstance(resolver);
				document = trans.transfom(new StreamSource(resource), resolver.getParams());
				type = MediaType.TEXT_HTML_TYPE;
				response = Response.ok().entity(document).type(MediaType.TEXT_HTML).build();
			} catch (TransformerException e) {
				log.error("Error while transforming resource", e);
				response = Response.serverError().build();
			} catch (ParserConfigurationException e) {
				log.error("Error while parsing resource", e);
				response = Response.serverError().build();
			}
		} else {
			response = Response.ok().entity(resource).type(type).build();
		}
		return response;
	}
}
