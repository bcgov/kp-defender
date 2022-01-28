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

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gov.ca.bc.qp.QPDefender.DAO.DAOCustType;
import gov.ca.bc.qp.QPDefender.beans.CustType;
import gov.ca.bc.qp.qpcommon.connection.DAOException;

/**
 * JAX-RS interface for accessing product information.
 * @author spencer.tickner
 */
@Path("{xsl:.+}/custtype")
public class WebCustType extends WebInterface {

	Logger log = LogManager.getLogger(getClass());
	
	// Grab our context to get our principal.
	/*
	@Context private SecurityContext securityContext;
	@Context private HttpHeaders header;
	@Context private UriInfo uriInfo;
	*/
	
	/**
	 * Helper method for ensuring we don't get null pointers if running outside a 
	 * 	security context.
	 * @return	A object representing the user accessing this interface.
	 */
	/*
	public QPPrincipal getPrincipal() {
		QPPrincipal principal = null;
		if(securityContext != null)
			principal = (QPPrincipal)securityContext.getUserPrincipal();
		return principal;
	}
	*/
	
	// Get our xslt path for transformations.
	@PathParam("xsl") public String xsl_global;
	
	/**
	 * Looks up all the customer types within the data source.
	 * @return A wrapped list of customer types.
	 */
	@GET
	@Path("all")
	@PermitAll
	public Response getCustTypes(@QueryParam("xsl") String optional_xsl, 
			@QueryParam("return_URI") String optional_Return_URI) {
		Response response = null;
		DAOCustType dao = new DAOCustType();
		/*
		Document doc = null;
		
		MediaType type = MediaType.APPLICATION_XML_TYPE;
		 */
		try {
			List<CustType> custType = dao.getAllCustType();
			response = this.getResponse(custType, optional_xsl, optional_Return_URI, null);
			/*
			QPMarshaller marshaller = new QPMarshaller();
			doc = marshaller.marshalToDomWrapped(custType, "types");
			if(!xsl_global.equals(DefaultResolver.NO_TRANSFORM)) {
				XSLTResolver resolver = new DefaultResolver(this.xsl_global, this.getPrincipal(), uriInfo, this);
				XSLTTransformer trans = XSLTTransformer.getInstance(resolver);
				doc = trans.transform(doc, resolver.getParams(null));
				type = MediaType.TEXT_HTML_TYPE;
			}
			response = Response.ok().entity(doc).type(type).build();
			*/
		/*	
		} catch (ParserConfigurationException e) {
			log.error("Parsing exception while looking up Customer Types", e);
			response = Response.serverError().build();
		} catch (JAXBException e) {
			log.error("JAXB exception while looking up Customer Types", e);
			response = Response.serverError().build();*/
		} catch (DAOException e) {
			log.error("Exception accessing data source while looking up Customer Types", e);
			response = Response.serverError().build();
		/*} catch (TransformerException e) {
			log.error("Transformation exception while looking up Customer Types", e);
			response = Response.serverError().build();*/
		} finally {}
		
		return response;
	}

	@Override
	public Logger getLogger() {
		// TODO Auto-generated method stub
		return this.log;
	}
}
