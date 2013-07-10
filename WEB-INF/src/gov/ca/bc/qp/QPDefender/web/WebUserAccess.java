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

import gov.ca.bc.qp.QPDefender.DAO.DAOAccess;
import gov.ca.bc.qp.QPDefender.DAO.InvalidCharacterException;
import gov.ca.bc.qp.QPDefender.config.MyRoles;
import gov.ca.bc.qp.qpcommon.authenticate.DAOUser;
import gov.ca.bc.qp.qpcommon.authenticate.User;
import gov.ca.bc.qp.qpcommon.authenticate.UserAccess;
import gov.ca.bc.qp.qpcommon.connection.DAOException;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

/**
 * JAX-RS interface for accessing user access information.
 * @author spencer.tickner
 */
@Path("{xsl:.+}/useraccess")
public class WebUserAccess extends WebInterface {

	Logger log = Logger.getLogger(this.getClass());
	
	@GET
	@Path("empty")
	public Response getEmptyUserAccess(@QueryParam("xsl") String optional_xsl, 
			@QueryParam("return_URI") String optional_Return_URI) {
		UserAccess ua = new UserAccess();
		return this.getResponse(ua, optional_xsl, optional_Return_URI, null);
		
	}
	
	@POST
	@Path("/add")
	@RolesAllowed({MyRoles.QP_SECURITY_GROUP_ADMIN, MyRoles.QP_ADMIN})
	public Response addUserAccess(UserAccess ua) {
		Response response = null;
		DAOAccess daoAccess = new DAOAccess();
		try {
			// Check to ensure the credentials meet our requirement.
			if(ua.getCredential() == null || ua.getCredential().length() < 4) {
				response = Response.status(Status.NOT_ACCEPTABLE).entity("Invalid credential").build();
			// Check to ensure this username does not exist
			} else if(this.usernameExists(ua.getUser().getUsername(), ua.getUser().getId())) {
				response = Response.status(Status.NOT_ACCEPTABLE).entity("Username exists").build();
			}
			// If everything is ok, add the user access.
			if(response != null) {
				daoAccess.AddUpdateUserAccess(ua);
				response = Response.ok().build();
			}
		} catch (DAOException e) {
			response = Response.serverError().build();
		} catch (InvalidCharacterException e) {
			response = Response.status(Status.NOT_ACCEPTABLE).entity("Invalid Characters").build();
		}
		return response;
	}

	@Override
	public Logger getLogger() {
		return log;
	}
}
