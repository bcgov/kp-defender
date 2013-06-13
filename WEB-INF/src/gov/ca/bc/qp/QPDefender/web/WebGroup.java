/*
 * Copyright (c) 2013, Queen's Printer of British Columbia, Canada and/or its affiliates. 
 * All rights reserved. DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE Header.
 * 
 * Please contact Queen's Printer of British Columbia, PO Box 9452 Stn Prov Govt, Victoria 
 * BC, V8W 9V7, (250) 387-3309 if you have any questions or have received this class in 
 * error.
 * 
 */
package gov.ca.bc.qp.QPDefender.web;


import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import gov.ca.bc.qp.QPDefender.DAO.DAOGroup;
import gov.ca.bc.qp.QPDefender.beans.Group;
import gov.ca.bc.qp.QPDefender.beans.GroupProduct;
import gov.ca.bc.qp.QPDefender.beans.UserAccess;
import gov.ca.bc.qp.QPDefender.config.MyResolver;
import gov.ca.bc.qp.QPDefender.config.MyRoles;
import gov.ca.bc.qp.QPDefender.utility.ObjectUtil;
import gov.ca.bc.qp.qpcommon.authenticate.DAOUser;
import gov.ca.bc.qp.qpcommon.authenticate.QPPrincipal;
import gov.ca.bc.qp.qpcommon.authenticate.User;
import gov.ca.bc.qp.qpcommon.code.ObjectNotFoundException;
import gov.ca.bc.qp.qpcommon.connection.DAOException;
import gov.ca.bc.qp.qpcommon.dom.DefaultResolver;
import gov.ca.bc.qp.qpcommon.dom.XSLTResolver;
import gov.ca.bc.qp.qpcommon.dom.XSLTTransformer;
import gov.ca.bc.qp.qpcommon.marshal.QPMarshaller;

import javax.annotation.security.RolesAllowed;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
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
 * JAX-RS interface for accessing group information
 * @author spencer.tickner
 *
 */
@Path("{xsl:.+}/groups")
public class WebGroup {

	Logger log = Logger.getLogger(getClass());
	
	// Grab our context to get our principal.
	@Context private SecurityContext securityContext;
	@Context private HttpHeaders header;
	@Context private UriInfo uriInfo;
	
	// Get our xslt path for transformations.
	@PathParam("xsl") public String xsl_global;
	
	// Used for testing
	protected QPPrincipal principal = null;
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
	
	static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
	/**
	 * Gets a group and it's associated access information.
	 * @param id The unique identifier for this group.
	 * @return The group and it's associated access and user information.
	 */
	@GET
	@Path("empty")
	@Produces({MediaType.TEXT_HTML, MediaType.APPLICATION_XML})
	@RolesAllowed({MyRoles.QP_ADMIN, MyRoles.QP_SECURITY_GROUP_ADMIN})
	public Response getEmptyGroup() {
		Response response = null;
		Group group = new Group();
		QPMarshaller marshaller = new QPMarshaller();
		try {
			Document doc = marshaller.marshalToDom(group);
			MediaType type = MediaType.TEXT_XML_TYPE;
			if(!xsl_global.equalsIgnoreCase(MyResolver.NO_TRANSFORM)) {
				MyResolver resolver = new MyResolver(this.xsl_global, this.getPrincipal(), this.uriInfo);
				XSLTTransformer trans = XSLTTransformer.getInstance(resolver);
				doc = trans.transform(doc, resolver.getParams());
				type = MediaType.TEXT_HTML_TYPE;
			}
			response = Response.ok().entity(doc).type(type).build();
		} catch (TransformerException e) {
			log.error("Error occured while transforming empty group with xsl " + this.xsl_global, e);
			response = Response.serverError().build();
		} catch (ParserConfigurationException e) {
			log.error("Error occurred while parsing empty group", e);
			response = Response.serverError().build();
		} catch (JAXBException e) {
			log.error("Error while marshalling empty group", e);
			response = Response.serverError().build();
		} finally {}
		
		return response;
	}
	
	/**
	 * Adds a group to our data source.
	 * @return TODO: What should this return, the group, new id, or redirect to the GroupAccess page.
	 */
	@POST
	@Path("add")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces({MediaType.TEXT_HTML, MediaType.TEXT_XML})
	@RolesAllowed({MyRoles.QP_ADMIN})
	public Response addGroup(
			@FormParam("id") String id,
			@FormParam("active") String s_active,
			@FormParam("custtype") String custtype,
			@FormParam("company_ministry") String company_ministry,
			@FormParam("dept_branch") String dept_branch,
			@FormParam("addr1") String addr1,
			@FormParam("addr2") String addr2,
			@FormParam("city") String city,
			@FormParam("prov") String prov,
			@FormParam("country") String country,
			@FormParam("pcode") String pcode,
			@FormParam("phone") String phone,
			@FormParam("fax") String fax,
			@FormParam("email") String email,
			@FormParam("contact_name") String contact_name,
			@FormParam("contact_phone") String contact_phone,
			@FormParam("contact_email") String contact_email,
			@FormParam("start_dt") String start_dt_s,
			@FormParam("expiry_dt") String expiry_dt_s,
			@FormParam("cust_note") String cust_no,
			@FormParam("package") String s_package,
			@FormParam("auto_expire") String auto_expire,
			@FormParam("daysleft") String daysleft,
			@FormParam("organisation_type") String organisation_type,
			@FormParam("contact_fax") String contact_fax,
			@FormParam("sap_order") String sap_order,
			@FormParam("sap_customer") String sap_customer) {
		Response response = null;
		DAOUser daoUser = new DAOUser();
		User user = null;
		Date start_dt = null;
		Date expiry_dt = null;
		int iCustType = -1;
		int iDaysLeft = -1;
		int iID = -1;
		boolean active = false;
		
		this.log.error("Group being added");
		
		// If the id is not an integer send server error, this should never happen.
		try {
			iID = Integer.parseInt(id);
		} catch(NumberFormatException e) {
			log.error("Invalid group identifier", e);
			return Response.serverError().build();
		}

		
		try {
			// Convert our dates, if applicable
			if(!(start_dt_s == null || start_dt_s.length()== 0))
				start_dt = sdf.parse(start_dt_s);
			else 
				start_dt = ObjectUtil.getEmptyDate();
			if(!(expiry_dt_s == null || expiry_dt_s.length()== 0))
				expiry_dt = sdf.parse(expiry_dt_s);
			else 
				expiry_dt = ObjectUtil.getEmptyDate();
			
			// Convert integers if applicable
			if(!(custtype == null || custtype.length() == 0))
				iCustType = Integer.parseInt(custtype);
			else 
				iCustType = -1;
			if(!(daysleft == null || daysleft.length() == 0))
				iDaysLeft = Integer.parseInt(daysleft);
			else 
				iDaysLeft = -1;
			
			// Convert boolean
			if(s_active.equalsIgnoreCase("on") || s_active.equalsIgnoreCase("true")) {
				active = true;
			}
			
			
			
			user = daoUser.lookupUserById(this.getPrincipal().getUserId());
			
			// We set right now for insert and modify date. If we are updating the insert date and
			//		insert user will simply be ignored.
			Group group = new Group(iID, active, iCustType,
					company_ministry, dept_branch, addr1, addr2, city, prov, country, pcode, phone,
					fax, email, contact_name, contact_phone, contact_email, start_dt, 
					expiry_dt, new Date(), user, new Date(), user, cust_no, s_package, 
					Boolean.parseBoolean(auto_expire), iDaysLeft, 
					organisation_type, contact_fax, sap_order, sap_customer, new ArrayList<UserAccess>(),
					new ArrayList<GroupProduct>());
			DAOGroup dao = new DAOGroup();
			
			// We'll add a update parameter to our xsl.
			String xsl= this.xsl_global;
			// If the id of this group is -1 it means we're adding a new group. If not we're updating.
			if(iID == -1) {
				iID = dao.addGroup(group);
				// Add our addition feedback.
				xsl = xsl + "/msg=" + URLEncoder.encode("Group Added", "UTF-8");
			} else {
				dao.updateGroup(group);
				// Add our update feedback.
				xsl = xsl + "/msg=" + URLEncoder.encode("Group Updated", "UTF-8");
			}
			
			
			// We are going to redirect to the new group that was created using the same xsl to render the content.
			String url = "/QPDefender/app/" + xsl + "/groups/ID/" + Integer.toString(iID);
			URI redirectURI = this.uriInfo.getBaseUri().resolve(url);
			response = Response.seeOther(redirectURI).build();
			//response = Response.ok().entity("Success").build();
		} catch (ObjectNotFoundException e) {
			log.warn("Error when adding a group, user not found", e);
			response = Response.status(Status.NOT_FOUND).build();
		} catch (DAOException e) {
			log.error("Error while accessing the database while adding a group.", e);
			response = Response.serverError().build();
		} catch (ParseException e) {
			log.warn("Invalid date format", e);
			response = Response.status(Status.BAD_REQUEST).build();
		} catch (UnsupportedEncodingException e) {
			log.error("URI format is incorrect", e);
			response = Response.status(Status.BAD_REQUEST).build();
		}
		
		return response;
	}
	
	/**
	 * Allows Group admins to access and manage their own group.
	 * @return The group that the current user belongs to.
	 */
	@GET
	@Path("me")
	@Produces({MediaType.TEXT_HTML, MediaType.APPLICATION_XML})
	@RolesAllowed({MyRoles.QP_ADMIN, MyRoles.QP_SECURITY_GROUP_ADMIN})
	public Response getMyGroup() {
		Response response = null;
		DAOUser dao = new DAOUser();
		User user = null;
		try {
			user = dao.lookupUserById(this.getPrincipal().getUserId());
			response = this.getGroup(Integer.toString(user.getGroupId()));
		} catch (ObjectNotFoundException e) {
			log.warn("User not found while looking up My Group for " + Integer.toString(this.getPrincipal().getUserId()), e);
			response = Response.status(Status.NOT_FOUND).build();
		} catch (DAOException e) {
			log.error("Data Access exception when accessing my group", e);
			response = Response.serverError().build();
		}
		
		return response;
	}
	
	/**
	 * Gets a group and it's associated access information.
	 * @param id The unique identifier for this group.
	 * @return The group and it's associated access and user information.
	 */
	@GET
	@Path("ID/{ID}")
	@Produces({MediaType.TEXT_HTML, MediaType.APPLICATION_XML})
	@RolesAllowed({MyRoles.QP_ADMIN})
	public Response getGroup(@PathParam("ID") String id) {
		Response response = null;
		DAOGroup dao = new DAOGroup();
		Group group = new Group();
		QPMarshaller marshaller = new QPMarshaller();
		try {
			group = dao.lookupGroup(Integer.parseInt(id));
			Document doc = marshaller.marshalToDom(group);
			MediaType type = MediaType.TEXT_XML_TYPE;
			// If we have a transformer set, resolve to HTML.
			if(!xsl_global.equalsIgnoreCase(MyResolver.NO_TRANSFORM)) {
				XSLTResolver resolver = new DefaultResolver(this.xsl_global, this.getPrincipal(), this.uriInfo, this);
				XSLTTransformer trans = XSLTTransformer.getInstance(resolver);
				
				byte[] b = trans.transformToByteArray(doc, resolver.getParams());
				type = MediaType.TEXT_HTML_TYPE;
				response = Response.ok().entity(b).type(type).build();
			} else {	
				response = Response.ok().entity(doc).type(type).build();
			}
		} catch (NumberFormatException e) {
			log.warn("Group id is not a integer", e);
			response = Response.status(Status.BAD_REQUEST).build();
		} catch (ObjectNotFoundException e) {
			log.warn("Group not found", e);
			response = Response.status(Status.NOT_FOUND).build();
		} catch (DAOException e) {
			log.error("Error when access our data source for group.", e);
			response = Response.serverError().build();
		} catch (JAXBException e) {
			log.error("Error when marshalling our group object.", e);
			response = Response.serverError().build();
		} catch (ParserConfigurationException e) {
			log.error("Error when marshalling our group object.", e);
			response = Response.serverError().build();
		} catch (TransformerException e) {
			log.error("Error while transforming group with stylesheet: " + this.xsl_global, e);
			response = Response.serverError().build();
		}
		
		
		return response;
	}
	
	
}
