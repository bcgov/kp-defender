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

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.List;

import gov.ca.bc.qp.QPDefender.DAO.DAOGroup;
import gov.ca.bc.qp.QPDefender.beans.CredentialType;
import gov.ca.bc.qp.QPDefender.config.ExternalResolver;
import gov.ca.bc.qp.qpcommon.authenticate.DAOUser;
import gov.ca.bc.qp.qpcommon.authenticate.QPPrincipal;
import gov.ca.bc.qp.qpcommon.authenticate.User;
import gov.ca.bc.qp.qpcommon.code.QPBean;
import gov.ca.bc.qp.qpcommon.connection.DAOException;
import gov.ca.bc.qp.qpcommon.dom.DefaultResolver;
import gov.ca.bc.qp.qpcommon.dom.XSLTResolver;
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
	
	// Name of the parameter we add any messages to.
	private static final String PARAM_MSG = "msg";
		
	// Grab our context to get our principal.
	// TODO: Change these to private once we moved all interface classes to extending this class.
	@Context protected SecurityContext securityContext;
	@Context protected HttpHeaders header;
	@Context protected UriInfo uriInfo;
	
	// Private member variable.
	protected QPPrincipal principal = null;
	private String optional_xsl = null;
	private String optional_Return_URI = null;
	
	/**
	 * @return An optional xsl parameter for overriding our xsl path parameter. Used when accessing project from an external project.
	 */
	private String getOptionalXsl() {
		return this.optional_xsl;
	}
	/**
	 * @param optional_xsl An optional xsl parameter for overriding our xsl path parameter. Used when accessing project from an external project.
	 */
	private void setOptionalXsl(String optional_xsl) {
		if(optional_xsl == null)
			optional_xsl = "";
		this.optional_xsl = optional_xsl;
	}
	/**
	 * @return An optional parameter for redirecting the user to a separate URL. The URL is relative to the domain. 
	 */
	private String getOptionalReturnURI() {
		return this.optional_Return_URI;
	}
	/**
	 * @param optional_Return_URI An optional parameter for redirecting the user to a separate URL. The URL is relative to the domain.
	 */
	private void setOptionalReturnURI(String optional_Return_URI) {
		if(optional_Return_URI == null)
			optional_Return_URI = "";
		this.optional_Return_URI = optional_Return_URI;
	}
	
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
	public Response getResponse(Document source, String optional_xsl, String optional_Return_URI, String[] messages) {
		
		// set our private member variables
		this.setOptionalXsl(optional_xsl);
		this.setOptionalReturnURI(optional_Return_URI);
		
		MediaType type = MediaType.TEXT_XML_TYPE;
		Response response = null;
		// Create a resolver
		XSLTResolver resolver = null;
		// If our xsl is our default keyword for no transform, or we have no source, skip resolving.
		try {
			if(!(xsl.equals(DefaultResolver.NO_TRANSFORM) || source == null)) {

				if(this.getOptionalXsl() != "") {
					resolver = new ExternalResolver(this.getOptionalXsl(),
							this.getPrincipal(), uriInfo, me);
				} else {
					resolver = new DefaultResolver(xsl, this.getPrincipal(), uriInfo, me);
				}

				XSLTTransformer trans = XSLTTransformer.getInstance(resolver);
				byte[] content = trans.transformToByteArray(source, resolver.getParams(messages));
				type = MediaType.TEXT_HTML_TYPE;
					
				response = Response.ok().entity(content).type(type).build();
				
			} else {
				response = Response.ok().entity(source).type(type).build();
			}
			
			// OK, if an optional redirect url was passed in we ignore everything we just did and redirect
			//		to the new url.
			if(!this.getOptionalReturnURI().equals("")) {
				URI redirectURI = null;
				// TODO: Review resolver as null, is this the best way to do this?
				if(resolver == null) {
					resolver = new DefaultResolver(DefaultResolver.NO_TRANSFORM, null, null, null);
				} 
				redirectURI = this.uriInfo.getBaseUri().resolve(resolver.createParamatizedURL(this.getOptionalReturnURI(), messages));
				response = Response.seeOther(redirectURI).build();
			}
		} catch (TransformerException e) {
			this.getLogger().error("XSLT exception occurred for " + this.xsl, e);
			response = Response.serverError().build();
		} catch (IOException e) {
			this.getLogger().error("Unable to resolve external xsl " + optional_xsl, e);
			response = Response.serverError().build();
		} finally {}
		return response;
	}

	
	/**
	 * Creates a response from a list of beans based on context resolver rules for the project. 
	 * 	It automatically wraps the list in the objects name.
	 * @param beans A list of beans to resolve.
	 * @return		A response representing the list of beans, or an response representing an exception if one occurs.
	 */
	public Response getResponse(List<? extends QPBean> beans, String optional_xsl, String optional_Return_URI, String[] messages)  {
		Document doc = null;
		Response response = null;
		if(beans.size() > 0) {
			// Create a wrapper for our list based on the class name with an s appended.
			String wrapper = beans.get(0).getClass().getSimpleName() + "s";
			QPMarshaller marshaller = new QPMarshaller();
			try {
				doc = marshaller.marshalToDomWrapped(beans, wrapper);
				response = this.getResponse(doc, optional_xsl, optional_Return_URI, messages);
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
	
	/**
	 * After performing a post we may want to simply redirect to a success page. Use this method.
	 * @param redirectURL	The url to redirect to (/whatever/app/etc)
	 * @param messages		Any 
	 * @return
	 */
	public Response getResponse(String redirectURL, String[] messages) {
		Response response = null;
		Document source = null;
		response = this.getResponse(source, null, redirectURL, messages);
		return response;
	}
	
	/**
	 * Creates a response from a bean based on context resolver rules for the project.
	 * @param bean	The bean to wrap in a response.
	 * @param optional_xsl An optional xsl for transforming the resulting xml document that overrides the xsl set in the path. Used
	 * 							mainly for external projects accessing this api.
	 * @param optional_Return_URI An optional parameter for redirecting the response to a location of api consumers choice.
	 * @param messages An array of system generated messages that will be passed to any xsl transformation that is invoked on success 
	 * 						of the transaction as a parameter named msg.
	 * @return A response representing the bean or a response representing an exception if one occurs.
	 */
	public Response getResponse(QPBean bean, String optional_xsl, String optional_Return_URI, String[] messages) {
		Document doc = null;
		Response response = null;
		QPMarshaller marshaller = new QPMarshaller();
		try {
			doc = marshaller.marshalToDom(bean);
			response = this.getResponse(doc, optional_xsl, optional_Return_URI, messages);
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
	 * Checks to see if this username already exists in the database. If the id is -1 this
	 * will return true if the username exists. If the id is not -1 it checks to see if another
	 * username exists in the database with a DIFFERENT id. If the id is the same it returns false,
	 * even though a username does exist in the database (ie the user with this id).
	 * @param username	English representation of a unique user.
	 * @param id		Unique identifier for this user.
	 * @return			Whether or not the username exists in the database who ISN'T that user.
	 * @throws DAOException An error occurred while accessing our data source.
	 */
	public boolean usernameExists(String username, int id) throws DAOException {
		boolean exists = false;
		DAOUser daoUser = new DAOUser();
		User user = daoUser.LookupUserByName(username);
		if(user == null) {
			exists = false;
		} else {
			if(id == -1)
				exists = true;
			else if(id != user.getId())
				exists = true;
		}
		return exists;
	}
	
	/**
	 * Updates a group to reflect the user that has modified it. Note that this method logs exceptions rather than
	 * pushing them back to the client.
	 * @param userid	The unique identifier for the user doing the modifying.
	 * @param groupid	The unique identifier for the group that is being modified.
	 */
	public void updateGroupUserModified(int userid, int groupid) {
		DAOGroup dao = new DAOGroup();
		try {
			dao.updateUserModified(userid, groupid);
		} catch (DAOException e) {
			this.getLogger().error("Unable to update User modifying the group");
		}
	}
	
	/**
	 * @return Logger for the implementing class.
	 */
	public abstract Logger getLogger();

}
