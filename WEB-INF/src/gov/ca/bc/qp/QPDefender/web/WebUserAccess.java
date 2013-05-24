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

import gov.ca.bc.qp.QPDefender.beans.UserAccess;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

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
	public Response getEmptyUserAccess() {
		UserAccess ua = new UserAccess();
		return this.getResponse(ua);
		
	}

	@Override
	public Logger getLogger() {
		return log;
	}
}
