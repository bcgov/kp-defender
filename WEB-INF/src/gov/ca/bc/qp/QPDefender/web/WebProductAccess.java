package gov.ca.bc.qp.QPDefender.web;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

import gov.ca.bc.qp.QPDefender.DAO.DAOAccess;
import gov.ca.bc.qp.QPDefender.DAO.DAOGroup;
import gov.ca.bc.qp.qpcommon.authenticate.ProductAccess;
import gov.ca.bc.qp.QPDefender.config.MyRoles;
import gov.ca.bc.qp.qpcommon.authenticate.DAOUser;
import gov.ca.bc.qp.qpcommon.authenticate.UserCredentials;
import gov.ca.bc.qp.qpcommon.code.ObjectNotFoundException;
import gov.ca.bc.qp.qpcommon.connection.DAOException;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

@Path("{xsl:.+}/productAccess")
public class WebProductAccess extends WebInterface {

	private static final Logger logger = Logger.getLogger(WebProductAccess.class);

	/** 
	 * @return An empty representation of a product that a user has access to.
	 */
	@GET
	@Path("empty")
	@RolesAllowed({MyRoles.QP_ADMIN, MyRoles.QP_SECURITY_GROUP_ADMIN})
	public Response getEmpty(@QueryParam("xsl") String optional_xsl, 
			@QueryParam("return_URI") String optional_Return_URI) {
		ProductAccess pa = new ProductAccess();
		return this.getResponse(pa, optional_xsl, optional_Return_URI, null);
	}
	
	/**
	 * Looks up product access by it's unique identifier.
	 * @param productAccessId	Unique identifier for representing a users access to a product.
	 * @return	Response representing a users access to a product.
	 */
	@GET
	@Path("ID/{paId}")
	@RolesAllowed({MyRoles.QP_ADMIN, MyRoles.QP_SECURITY_GROUP_ADMIN})
	public Response getProductAccessById(
			@PathParam("paId") String productAccessId,
			@QueryParam("xsl") String optional_xsl, 
			@QueryParam("return_URI") String optional_Return_URI) {
		DAOAccess dao = new DAOAccess();
		Response response = null;
		try {
			response = this.getResponse(
					dao.lookupProductAccessById(Integer.parseInt(productAccessId)), 
					optional_xsl, optional_Return_URI, null);
		} catch (NumberFormatException e) {
			this.getLogger().error("Product Access Id is not an integer: " + productAccessId, e);
			response = Response.status(Status.BAD_REQUEST).build();
		} catch (DAOException e) {
			this.getLogger().error("Error accessing our data source while looking up product access by id", e);
			response = Response.serverError().build();
		}
		return response;
	
	}
	
	@POST
	@Path("delete/ID/{paId}")
	@RolesAllowed({MyRoles.QP_ADMIN})
	public Response deleteProductAccessById(
			@PathParam("paId") String productAccessId,
			@FormParam("xsl") String optional_xsl, 
			@FormParam("return_URI") String optional_Return_URI) {
		DAOAccess dao = new DAOAccess();
		DAOGroup daoGroup = new DAOGroup();
		Response response = null;
		String message[];
		try {
			int i_productAccessId = Integer.parseInt(productAccessId);
			int userid = dao.lookupProductAccessById(i_productAccessId).getUserid();
			int groupid = daoGroup.lookupGroupByUserId(userid).getId();
			dao.deleteProductAccess(i_productAccessId);
			String redirect = this.xsl;
			// Construct our redirection string.
			//redirect = xsl + "/msg=" + URLEncoder.encode("Product Access Deleted", "UTF-8");
			message = new String[]{"Product Access Deleted"};
			// We are going to redirect to the new group that was created using the same xsl to render the content.
			/*
			String url = "/QPDefender/app/" + redirect + "/groups/ID/" + Integer.toString(groupid);
			URI redirectURI = this.uriInfo.getBaseUri().resolve(url);
			response = Response.seeOther(redirectURI).build();
			*/
			response = this.getResponse(optional_Return_URI, message);
		} catch (DAOException e) {
			this.getLogger().error("Problem accessing data source while deleting product access", e);
			response = Response.serverError().build();
		} catch (ObjectNotFoundException e) {
			this.getLogger().error("Problem when looking up group information while deleting product access", e);
			response = Response.serverError().build();
		} finally {}
		
		return response;
	}
	
	@POST
	@Path("userproduct/add")
	@RolesAllowed({MyRoles.QP_ADMIN, MyRoles.QP_SECURITY_GROUP_ADMIN})
	public Response addUserProduct(@FormParam("userCredId") String userCredId, 
			@FormParam("userProductId") String userProductId,
			@FormParam("groupProductId") String groupProductId,
			@FormParam("timeout") String timeout,
			@FormParam("active") String active,
			@FormParam("xsl") String optional_xsl, 
			@FormParam("return_URI") String optional_Return_URI) {
		DAOAccess dao = new DAOAccess();
		Response response = null;
		String[] messages;
		int i_userCredId = Integer.parseInt(userCredId);
		int i_userProductId = Integer.parseInt(userProductId);
		int i_timeout = Integer.parseInt(timeout);
		int i_groupProductId = Integer.parseInt(groupProductId);
		boolean b_active = false;
		if(active.equals("on"))
			b_active = true;
		else 
			b_active = Boolean.getBoolean(active);
		
		String redirect = this.xsl;
		
		DAOUser daouser = new DAOUser();
		try {
			// Start by looking up user credential information to get access to the user.
			UserCredentials uc = daouser.LookupUserCredentialsById(i_userCredId);
			dao.AddUpdateUserProduct(i_userProductId, uc.getUser().getId(), i_groupProductId, i_timeout, b_active);
			// Construct our redirection string.
			//redirect = xsl + "/msg=" + URLEncoder.encode("Product Access Updated", "UTF-8");
			messages = new String[]{"Product Access Updated"};
			// We are going to redirect to the new group that was created using the same xsl to render the content.
			/*
			String url = "/QPDefender/app/" + redirect + "/groups/ID/" + Integer.toString(uc.getUser().getGroupId());
			URI redirectURI = this.uriInfo.getBaseUri().resolve(url);
			response = Response.seeOther(redirectURI).build();
			*/
			return this.getResponse(optional_Return_URI, messages);
		} catch (DAOException e) {
			this.getLogger().error("Error accessing data source when adding user product", e);
			response = Response.serverError().build();
		} catch (ObjectNotFoundException e) {
			this.getLogger().error("Error, no user credentials when adding user product", e);
			response = Response.serverError().build();
		/*} catch (UnsupportedEncodingException e) {
			this.getLogger().warn("Error encoding our redirection url when adding user product", e);
			response = Response.status(Status.BAD_REQUEST).build();*/
		} finally {}
			
		
		return response;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}
	
	
}
