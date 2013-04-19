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


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.ca.bc.qp.QPDefender.DAO.DAOGroup;
import gov.ca.bc.qp.QPDefender.beans.Group;
import gov.ca.bc.qp.QPDefender.beans.GroupProduct;
import gov.ca.bc.qp.QPDefender.beans.UserAccess;
import gov.ca.bc.qp.QPDefender.config.MyResolver;
import gov.ca.bc.qp.QPDefender.config.MyRoles;
import gov.ca.bc.qp.qpcommon.authenticate.DAOUser;
import gov.ca.bc.qp.qpcommon.authenticate.QPPrincipal;
import gov.ca.bc.qp.qpcommon.authenticate.User;
import gov.ca.bc.qp.qpcommon.code.ObjectNotFoundException;
import gov.ca.bc.qp.qpcommon.connection.DAOException;
import gov.ca.bc.qp.qpcommon.dom.XSLTTransformer;
import gov.ca.bc.qp.qpcommon.marshal.QPMarshaller;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
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
				MyResolver resolver = new MyResolver(this.xsl_global, this.getPrincipal());
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
	@Produces({MediaType.TEXT_HTML, MediaType.APPLICATION_XML})
	@RolesAllowed({MyRoles.QP_ADMIN})
	public Response addGroup(
			@FormParam("active") String active,
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
			@FormParam("start_dt") Date start_dt,
			@FormParam("expiry_dt") Date expiry_dt,
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
		try {
			user = daoUser.lookupUserById(this.getPrincipal().getUserId());
			Group group = new Group(-1, Boolean.parseBoolean(active), Integer.parseInt(custtype),
					company_ministry, dept_branch, addr1, addr2, city, prov, country, pcode, phone,
					fax, email, contact_name, contact_phone, contact_email, start_dt, 
					expiry_dt, new Date(), user, new Date(), user, cust_no, s_package, 
					Boolean.parseBoolean(auto_expire), Integer.parseInt(daysleft), 
					organisation_type, contact_fax, sap_order, sap_customer, new ArrayList<UserAccess>(),
					new ArrayList<GroupProduct>());
		} catch (ObjectNotFoundException e) {
			log.warn("Error when adding a group, user not found", e);
			response = Response.status(Status.NOT_FOUND).build();
		} catch (DAOException e) {
			log.error("Error while accessing the database while adding a group.", e);
			response = Response.serverError().build();
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
				MyResolver resolver = new MyResolver(this.xsl_global, this.getPrincipal());
				XSLTTransformer trans = XSLTTransformer.getInstance(resolver);
				doc = trans.transform(doc, resolver.getParams());
				type = MediaType.TEXT_HTML_TYPE;
			}
			
			response = Response.ok().entity(doc).type(type).build();
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
