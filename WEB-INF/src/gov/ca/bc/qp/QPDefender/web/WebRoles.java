package gov.ca.bc.qp.QPDefender.web;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.List;

import gov.ca.bc.qp.QPDefender.DAO.DAOGroup;
import gov.ca.bc.qp.QPDefender.config.MyRoles;
import gov.ca.bc.qp.qpcommon.authenticate.DAORoles;
import gov.ca.bc.qp.qpcommon.authenticate.Role;
import gov.ca.bc.qp.qpcommon.code.ObjectNotFoundException;
import gov.ca.bc.qp.qpcommon.connection.DAOException;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

@Path("{xsl:.+}/roles")
public class WebRoles extends WebInterface {
	private static final Logger logger = Logger.getLogger(WebRoles.class);
	
	@GET
	@Path("/userrole/empty")
	@RolesAllowed({MyRoles.QP_ADMIN, MyRoles.QP_SECURITY_GROUP_ADMIN})
	public Response getEmptyUserRole() {
		return this.getResponse(new Role());
	}
	
	@GET
	@Path("/productid/{productid}")
	@RolesAllowed({MyRoles.QP_ADMIN, MyRoles.QP_SECURITY_GROUP_ADMIN})
	public Response getRolesForProduct(@PathParam("productid") String productid) {
		DAORoles dao = new DAORoles();
		int i_productid = Integer.parseInt(productid);
		Response response = null;
		try {
			response = this.getResponse(dao.lookupRolesByProductId(i_productid));
		} catch (DAOException e) {
			this.getLogger().error("Error when accessing our datasource while looking up product roles", e);
			response = Response.serverError().build();
		}
		return response;
	
	}
	
	@POST
	@Path("/userrole/add")
	@RolesAllowed({MyRoles.QP_ADMIN, MyRoles.QP_SECURITY_GROUP_ADMIN})
	public Response addUserRole(@FormParam("userid") String userid, 
			@FormParam("productId") String productId, 
			@FormParam("roleName") String roleName) {
		DAORoles dao = new DAORoles();
		DAOGroup daoGroup = new DAOGroup();
		Response response = null;
		int i_userid = Integer.parseInt(userid);
		int i_productid = Integer.parseInt(productId);
		String redirect = this.xsl;
		Role role = new Role(i_userid, i_productid, roleName);
		try {
			// First we ensure this user doesn't already have access to this product role.
			List<Role> roles = dao.getRolesByUserAndProduct(i_userid, i_productid);
			boolean exists = false;
			for(int i = 0; i < roles.size(); i++) {
				if(roles.get(i).getRoleName().equalsIgnoreCase(roleName)) {
					exists = true;
					break;
				}
			}
			if(exists) {
				// Role already exists for this user, send back an error message
				redirect = xsl + "/msg=" + URLEncoder.encode("Warning, User alreday has role, no access was added.", "UTF-8");
			} else {
				// Role in new, add it and send back nice message.
				dao.AddUserRole(role);
				// Construct our redirection string.
				redirect = xsl + "/msg=" + URLEncoder.encode("Role Added", "UTF-8");
			}
			// We are going to redirect to the new group that was created using the same xsl to render the content.
			String url = "/QPDefender/app/" + redirect + "/groups/ID/" + Integer.toString(daoGroup.lookupGroupByUserId(i_userid).getId());
			URI redirectURI = this.uriInfo.getBaseUri().resolve(url);
			response = Response.seeOther(redirectURI).build();
		} catch (DAOException e) {
			this.getLogger().error("Error accessing data source while adding a User Role", e);
			response = Response.serverError().build();
		} catch (ObjectNotFoundException e) {
			this.getLogger().warn("Group doesn't exist for user " + userid, e);
			response = Response.status(Status.BAD_REQUEST).build();
		} catch (UnsupportedEncodingException e) {
			this.getLogger().warn("URI encoding exception when adding User Role", e);
			response = Response.status(Status.BAD_REQUEST).build();
		} finally {}
		
		return response;
	}

	@Override
	public Logger getLogger() {
		return logger;
	}
}
