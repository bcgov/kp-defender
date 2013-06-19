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

import java.text.SimpleDateFormat;
import java.util.List;

import gov.ca.bc.qp.QPDefender.beans.CredentialType;
import gov.ca.bc.qp.qpcommon.authenticate.QPPrincipal;
import gov.ca.bc.qp.qpcommon.code.QPBean;
import gov.ca.bc.qp.qpcommon.dom.DefaultResolver;
import gov.ca.bc.qp.qpcommon.dom.XSLTTransformer;
import gov.ca.bc.qp.qpcommon.marshal.QPMarshaller;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

/**
 * Utility class for common functionality across Web interfaces. A note that this class
 * 	does not throw any exceptions but rather logs them and returns them wrapped in a response.
 *  If calling logic needs to determine if an exception occurred the Response status will be
 *  something other than ok().
 * @author spencer.tickner
 *
 */
public abstract class WebInterface {
	
	/**
	 * Our DefaultResolver class needs to know which project is calling it in order to resolve xsl paths to that
	 * 	projects classpath. We'll send in an instance of ourselves each time to ensure the resolver knows where to
	 *  resolve to.
	 */
	private static final WebInterface me = new WebInterface(){
		@Override
		public Logger getLogger() {
			// TODO Auto-generated method stub
			return null;
		}};
	
	// This must be protected to implementing classes have access
	protected static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
		
	// Grab our context to get our principal.
	// TODO: Change these to private once we moved all interface classes to extending this class.
	@Context protected SecurityContext securityContext;
	@Context protected HttpHeaders header;
	@Context protected UriInfo uriInfo;
	
	// Private member variable.
	protected QPPrincipal principal = null;
	
	/**
	 * @param principal The entity accessing this resource. This set method is a convenience method for
	 * 					unit testing to set their own principal outside an application server container.
	 */
	protected void setPrincipal(QPPrincipal principal) {
		this.principal = principal;
	}
	/**
	 * Helper method for ensuring we don't get null pointers if running outside a 
	 * 	security context.
	 * @return	A object representing the user accessing this interface.
	 */
	public QPPrincipal getPrincipal() {
		if(securityContext != null && this.principal == null)
			this.setPrincipal((QPPrincipal)securityContext.getUserPrincipal());
		return principal;
	}
	
	// Get our xslt path for transformations.
	@PathParam("xsl") public String xsl;

	/**
	 * Helper method for resolving source documents based on roles and transfomation rules.
	 * @param xsl		Path to an xsl document to transform the xsl. Rules for path structure are dictated by our DefaultResolver.
	 * @param source	The source xml document we will become the entity for the response.
	 * @param principal	The user who is attempting to access this resource.
	 * @param uriInfo	Uri information on the request being made.
	 * @return			A response object with entity, type and status set.
	 * @throws TransformerException Error occurred while transforming our source document.
	 */
	public Response getResponse(Document source) {
		MediaType type = MediaType.TEXT_XML_TYPE;
		Response response = null;
		if(!xsl.equals(DefaultResolver.NO_TRANSFORM)) {
			DefaultResolver resolver = new DefaultResolver(xsl, this.getPrincipal(), uriInfo, me);
			try {
				XSLTTransformer trans = XSLTTransformer.getInstance(resolver);
				byte[] content = trans.transformToByteArray(source, resolver.getParams());
				type = MediaType.TEXT_HTML_TYPE;
				response = Response.ok().entity(content).type(type).build();
			} catch (TransformerException e) {
				this.getLogger().error("XSLT exception occurred for " + this.xsl, e);
				response = Response.serverError().build();
			} finally {}
		} else {
			response = Response.ok().entity(source).type(type).build();
		}
		return response;
	}
	
	/**
	 * Creates a response from a list of beans based on context resolver rules for the project. 
	 * 	It automatically wraps the list in the objects name.
	 * @param beans A list of beans to resolve.
	 * @return		A response representing the list of beans, or an response representing an exception if one occurs.
	 */
	public Response getResponse(List<? extends QPBean> beans)  {
		Document doc = null;
		Response response = null;
		if(beans.size() > 0) {
			// Create a wrapper for our list based on the class name with an s appended.
			String wrapper = beans.get(0).getClass().getSimpleName() + "s";
			QPMarshaller marshaller = new QPMarshaller();
			try {
				doc = marshaller.marshalToDomWrapped(beans, wrapper);
				response = this.getResponse(doc);
			} catch (ParserConfigurationException e) {
				this.getLogger().error("Parsing Exception marshalling list " + wrapper, e);
				response = Response.serverError().build();
			} catch (JAXBException e) {
				this.getLogger().error("JAXB Exception when marshalling " + wrapper, e);
				response = Response.serverError().build();
			} finally {}
		} else {
			response = Response.noContent().build();
		}
		return response;
	}
	
	public Response getResponse(String redirectURL) {
		Response response = null;
		return response;
	}
	
	/**
	 * Creates a response from a bean based on context resolver rules for the project.
	 * @param bean	The bean to wrap in a response.
	 * @return A response representing the bean or a response representing an exception if one occurs.
	 */
	public Response getResponse(QPBean bean) {
		Document doc = null;
		Response response = null;
		QPMarshaller marshaller = new QPMarshaller();
		try {
			doc = marshaller.marshalToDom(bean);
			response = this.getResponse(doc);
		} catch (ParserConfigurationException e) {
			this.getLogger().error("Parsing Exception marshalling bean " + bean.getClass().getSimpleName(), e);
			response = Response.serverError().build();
		} catch (JAXBException e) {
			this.getLogger().error("JAXB Exception when marshalling " + bean.getClass().getSimpleName(), e);
			response = Response.serverError().build();
		}
		return response;
	}
	
	/**
	 * @return Logger for the implementing class.
	 */
	public abstract Logger getLogger();

}
