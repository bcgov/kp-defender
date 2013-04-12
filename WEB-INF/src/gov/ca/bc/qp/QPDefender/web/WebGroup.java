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


import gov.ca.bc.qp.QPDefender.DAO.DAOGroup;
import gov.ca.bc.qp.QPDefender.beans.Group;
import gov.ca.bc.qp.QPDefender.config.MyResolver;
import gov.ca.bc.qp.qpcommon.code.ObjectNotFoundException;
import gov.ca.bc.qp.qpcommon.connection.DAOException;
import gov.ca.bc.qp.qpcommon.marshal.QPMarshaller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

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
	@PathParam("xsl") public String xsl_global = "";
	
	/**
	 * Gets a group and it's associated access information.
	 * @param id The unique identifier for this group.
	 * @return The group and it's associated access and user information.
	 */
	@GET
	@Path("{ID}")
	public Response getGroup(@PathParam("ID") String id) {
		Response response = null;
		DAOGroup dao = new DAOGroup();
		Group group = new Group();
		QPMarshaller marshaller = new QPMarshaller();
		try {
			group = dao.lookupGroup(Integer.parseInt(id));
			Document doc = marshaller.marshalToDom(group);
			MediaType type = MediaType.TEXT_HTML_TYPE;
			if(xsl_global.equalsIgnoreCase(MyResolver.NO_TRANSFORM))
				type = MediaType.TEXT_XML_TYPE;
			
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
		}
		
		
		return response;
	}
	
	
}
