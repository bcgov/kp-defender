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

import gov.ca.bc.qp.QPDefender.DAO.DAOCredentialType;
import gov.ca.bc.qp.qpcommon.connection.DAOException;
import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;

/**
 * JAX-RS interface for accessing product information.
 * @author spencer.tickner
 */
@Path("{xsl:.+}/credentialtype")
public class WebCredentialType extends WebInterface {

	Logger log = Logger.getLogger(getClass());
	
	/**
	 * Looks up all the customer types within the data source.
	 * @return A wrapped list of customer types.
	 */
	@GET
	@Path("all")
	@PermitAll
	public Response getCredentialTypes() {
		DAOCredentialType dao = new DAOCredentialType();
		Response response = null;
		try {
			response = this.getResponse(dao.getAllCredentialTypes());
		} catch (DAOException e) {
			log.error("Exception accessing our database for credential types", e);
			response = Response.serverError().build();
		}
		return response;
	}

	@Override
	public Logger getLogger() {
		return this.log;
	}
}
