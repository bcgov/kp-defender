package gov.ca.bc.qp.QPDefender.web;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Date;

import gov.ca.bc.qp.QPDefender.DAO.DAOGroup;
import gov.ca.bc.qp.QPDefender.DAO.DAOGroupProduct;
import gov.ca.bc.qp.QPDefender.beans.GroupProduct;
import gov.ca.bc.qp.QPDefender.config.MyRoles;
import gov.ca.bc.qp.qpcommon.authenticate.DAOProducts;
import gov.ca.bc.qp.qpcommon.authenticate.Product;
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

@Path("{xsl:.+}/groupproducts")
public class WebGroupProduct extends WebInterface {

	static final Logger log = Logger.getLogger(WebGroupProduct.class);
	
	/**
	 * Looks up the products that a group has access to via that groups unique identifier.
	 * @param groupid The unique identifier that represents a group.
	 * @return A response detailing the products that this group has access to.
	 */
	@GET
	@Path("/groupid/{id}")
	@RolesAllowed({MyRoles.QP_ADMIN})
	public Response getGroupProducts(
			@PathParam("id") String groupid,
			@QueryParam("xsl") String optional_xsl, 
			@QueryParam("return_URI") String optional_Return_URI) {
		DAOGroupProduct dao = new DAOGroupProduct();
		int iGroupid = Integer.parseInt(groupid);
		Response response = null;
		try {
			response = this.getResponse(
					dao.lookupGroupProductByGroupId(iGroupid), optional_xsl, optional_Return_URI, null);
		} catch (DAOException e) {
			log.error("Exception occurred when looking up group products for group " + groupid, e);
			response = Response.serverError().build();
		}
		return response;
	}
	
	/**
	 * Looks up the products the group of the currently logged in principal has membership in, has access to.
	 * @return A response detailing the products the current prinicpal's group has access to.
	 */
	@GET
	@Path("/groupid/me")
	@RolesAllowed({MyRoles.QP_ADMIN, MyRoles.QP_SECURITY_GROUP_ADMIN})
	public Response getGroupProducts(@QueryParam("xsl") String optional_xsl, 
			@QueryParam("return_URI") String optional_Return_URI) {
		return this.getGroupProducts(
				Integer.toString(this.getPrincipal().getGroupId()),
				optional_xsl, optional_Return_URI);
	}
	
	/**
	 * Looks up a product and it's group information based on it's unique identifier.
	 * @param gpid	Unique identifier for the product and it's specific group information.
	 * @return A response with the group product information or an empty object if it does not exist.
	 */
	@GET
	@Path("ID/{id}")
	@RolesAllowed({MyRoles.QP_ADMIN, MyRoles.QP_SECURITY_GROUP_ADMIN})
	public Response getGroupProductById(@PathParam("id") String gpid, 
			@QueryParam("xsl") String optional_xsl, 
			@QueryParam("return_URI") String optional_Return_URI) {
		// TODO: Security risk, ensure if the principal has a role of security group admin they cannot access group
		//			products outside of their own group.
		DAOGroupProduct dao = new DAOGroupProduct();
		int iGPid = Integer.parseInt(gpid);
		Response response = null;
		try {
			response = this.getResponse(dao.lookupGroupProductById(iGPid),
					optional_xsl, optional_Return_URI, null);
		} catch (DAOException e) {
			log.error("An exception occurred when looking up group product " + gpid, e);
			response = Response.serverError().build();
		} catch (ObjectNotFoundException e) {
			response = this.getEmpty(optional_xsl, optional_Return_URI);
		}
		return response;
	}
	
	
	/**
	 * Adds a product to the available products for users who are members of this group.
	 * @param id		Unique identifier of this groups associated product. If -1 an additional will occur, if not a update.
	 * @param groupid	The group we are giving access to this product to.
	 * @param productid	Unique identifier of this product.
	 * @param concurrent Amount of users for this group that can access this product concurrently.
	 * @param expiryDate The date that this product is not longer active for this group.
	 * @return
	 */
	@POST
	@Path("add")
	@RolesAllowed({MyRoles.QP_ADMIN})
	public Response add(
			@FormParam("id") String id,
			@FormParam("groupid") String groupid,
			@FormParam("productType") String productid,
			@FormParam("concurrent") String concurrent,
			@FormParam("expiryDate") String expiryDate, 
			@FormParam("xsl") String optional_xsl, 
			@FormParam("return_URI") String optional_Return_URI) {
		
		Response response = null;
		
		int iId = Integer.parseInt(id);
		int iGroupId = Integer.parseInt(groupid);
		int iProductId = Integer.parseInt(productid);
		int iConcurrent = Integer.parseInt(concurrent);
		DAOProducts daoP = new DAOProducts();
		DAOGroup daoGroup = new DAOGroup();
		String[] messages;
		// We'll add a update parameter to our xsl.
		//String redirect = this.xsl;
		try {
			// First we check to see if this user already has access to this product. If so, return message
			//		informing the group that the cannot add products they already have access to.
			if(iId < 0 && daoGroup.isProductAssociatedToGroup(iProductId, iGroupId)) {
				messages = new String[]{"Error Product was not added Group already has access"};
			} else {
				Date dExpiry = sdf.parse(expiryDate);
				Product p = daoP.lookupProductById(iProductId);
				GroupProduct gp = new GroupProduct(iId, iGroupId, p, iConcurrent, dExpiry);
				daoGroup.AddProductToGroup(gp);
				
				
				// If the id of this group is -1 it means we're adding a new group. If not we're updating.
				if(iId == -1) {
					// Add our addition feedback.
					messages = new String[]{"Product Added"};
				} else {
					// Add our update feedback.
					messages = new String[]{"Product Updated"};
				}
				// Update the fact that the group has been modified.
				this.updateGroupUserModified(this.getPrincipal().getUserId(), iGroupId);
			}
			// We are going to redirect to the new group that was created using the same xsl to render the content.
			/*
			String url = "/QPDefender/app/" + redirect + "/groups/ID/" + Integer.toString(iGroupId);
			URI redirectURI = this.uriInfo.getBaseUri().resolve(url);
			response = Response.seeOther(redirectURI).build();
			*/
			this.getResponse(optional_Return_URI, messages);
		} catch (DAOException e) {
			log.error("Error accessing our Data Source when adding group product: " + id, e);
			response = Response.serverError().build();
		} catch (ParseException e) {
			log.error("Error when adding group product while parsing date:" + expiryDate, e);
			response = Response.serverError().build();
		} finally{}
		
		
		return response;
		
		
	}
	
	/**
	 * Deletes a groups access to a product by that accesses unique identifier.
	 * @param id	Unique identifier to the access for a group for a product.
	 * @return		Response redirecting back to the group with the passed in stylesheet.
	 */
	@GET
	@Path("delete/{id}")
	@RolesAllowed({MyRoles.QP_ADMIN})
	public Response deleteGroup(@PathParam("id") String id,
			@QueryParam("xsl") String optional_xsl, 
			@QueryParam("return_URI") String optional_Return_URI) {
		DAOGroupProduct dao = new DAOGroupProduct();
		Response response = null;
		String[] messages;
		try {
			int groupid = -1;
			try {
				dao.lookupGroupProductById(Integer.parseInt(id)).getGroupid();
			} catch(Exception e) {
				// Abstract the exception as the only reason we're doing this is for updating the modified user
				this.getLogger().error("Error looking up group by id for use in the modify user update.", e);
			}
			dao.deleteGroupProduct(Integer.parseInt(id));
			messages = new String[]{"Product Access Deleted"};
			// We are going to redirect to the new group that was created using the same xsl to render the content.
			//String url = "/QPDefender/app/" + redirect + "/groups/ID/" + Integer.toString(this.getPrincipal().getGroupId());
			//URI redirectURI = this.uriInfo.getBaseUri().resolve(url);
			//response = Response.seeOther(redirectURI).build();
			response = this.getResponse(optional_Return_URI, messages);
			// Update the fact that this group has been modified.
			if(groupid != -1)
				this.updateGroupUserModified(this.getPrincipal().getUserId(), groupid);
		/*
		} catch (UnsupportedEncodingException e) {
			log.warn("Encoding exception when deleting group " + id, e);
			response = Response.status(Status.BAD_REQUEST).build(); */
		} catch (NumberFormatException e) {
			log.warn("Number formatting exception when deleteing group " + id, e);
			response = Response.status(Status.BAD_REQUEST).build();
		} catch (DAOException e) {
			log.error("Error while accessing our datasource to delete group", e);
			response = Response.serverError().build();
		} finally {}
		
		return response;
	}
	/**
	 * Gets an empty group product for adding.
	 * @return
	 */
	@GET
	@Path("empty")
	@RolesAllowed({MyRoles.QP_ADMIN, MyRoles.QP_SECURITY_GROUP_ADMIN})
	public Response getEmpty(
			@QueryParam("xsl") String optional_xsl, 
			@QueryParam("return_URI") String optional_Return_URI) {
		return this.getResponse(new GroupProduct(), optional_xsl, optional_Return_URI, null);
	}

	@Override
	public Logger getLogger() {
		// TODO Auto-generated method stub
		return log;
	}
	
	
}
