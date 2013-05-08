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

import javax.annotation.security.RolesAllowed;
import javax.servlet.ServletContext;
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
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import gov.ca.bc.qp.QPDefender.config.MyResolver;
import gov.ca.bc.qp.QPDefender.config.MyRoles;
import gov.ca.bc.qp.qpcommon.authenticate.Product;
import gov.ca.bc.qp.qpcommon.authenticate.QPPrincipal;
import gov.ca.bc.qp.qpcommon.connection.DAOException;
import gov.ca.bc.qp.qpcommon.dom.XSLTTransformer;
import gov.ca.bc.qp.qpcommon.marshal.QPMarshaller;

/**
 * JAX-RS interface for accessing product information.
 * @author spencer.tickner
 */
@Path("{xsl:.+}/products")
public class WebProduct {

	Logger log = Logger.getLogger(getClass());

	// Grab our context to get our principal.
	@Context private SecurityContext securityContext;
	@Context private HttpHeaders header;
	@Context private UriInfo uriInfo;
	
	/**
	 * Helper method for ensuring we don't get null pointers if running outside a 
	 * 	security context.
	 * @return	A object representing the user accessing this interface.
	 */
	public QPPrincipal getPrincipal() {
		QPPrincipal principal = null;
		if(securityContext != null)
			principal = (QPPrincipal)securityContext.getUserPrincipal();
		return principal;
	}
	
	// Get our xslt path for transformations.
	@PathParam("xsl") public String xsl_global;
	
	@POST
	@Path("/add")
	@Produces({MediaType.TEXT_HTML, MediaType.APPLICATION_XML})
	@RolesAllowed(MyRoles.QP_ADMIN)
	public Response addProduct(@FormParam("productname") String productname,
			@FormParam("urlpattern") String urlpattern,
			@FormParam("defaulttimeout") String defaulttimeout) {
		Response response = null;
		
		return response;
		
	}
	
	/**
	 * @return a list of products and their associated information.
	 */
	@GET
	@Path("/all")
	@Produces({MediaType.TEXT_HTML, MediaType.APPLICATION_XML})
	@RolesAllowed({MyRoles.QP_ADMIN})
	public Response getProducts() {
		MediaType type = MediaType.APPLICATION_XML_TYPE;
		QPMarshaller marshaller = new QPMarshaller();
		Response response = null;
		try {
			// Get a list of products
			List<Product> products = Product.getProducts();
			// Marshal them with a wrapper.
			Document document = marshaller.marshalToDomWrapped(products, "products");
			// Transform our xsl
			if(!xsl_global.equals(MyResolver.NO_TRANSFORM)) {
				MyResolver resolver = new MyResolver(this.xsl_global, this.getPrincipal(), this.uriInfo);
				XSLTTransformer trans = XSLTTransformer.getInstance(resolver);
				document = trans.transform(document, resolver.getParams());
				type = MediaType.TEXT_HTML_TYPE;
			}
			response = null;
			response = Response.ok().entity(document).type(type).build();
		} catch (JAXBException e) {
			log.error("Exception getting list of products.", e);
			response = Response.serverError().build();
		} catch (ParserConfigurationException e) {
			log.error("Exception when marshalling a list of products.", e);
			response = Response.serverError().build();
		} catch (DAOException e) {
			log.error("Exception when accessing our data source for list of products.", e);
			response = Response.serverError().build();
		} catch (TransformerConfigurationException e) {
			log.error("Exception creating our transformer", e);
			response = Response.serverError().build();
		} catch (TransformerException e) {
			log.error("Exception creating our transformer", e);
			response = Response.serverError().build();
		}
		return response;
	}
	
	@GET
	@Path("/ID/{id}")
	@RolesAllowed({"qpadmin", "qpsecurity.guest"})
	public Response getProductById(@PathParam("id") String id) {
		Response response = null;
		Product product = null;
		Document doc = null;
		MediaType type = MediaType.TEXT_XML_TYPE;
		QPMarshaller marshaller = new QPMarshaller();
		
		try {
			// We don't have a lookup by id, so loop through and get the product with matching id.
			List<Product> products = Product.getProducts();
			for(int i = 0; i < products.size(); i++) {
				if(products.get(i).getId() == Integer.parseInt(id)) {
					product = products.get(i);
					// found it, break
					break;
				}
			}
			
			// We should not have products looked up that we can't find.
			if(product == null) {
				response = Response.status(Status.NOT_FOUND).build();
				log.warn("Product lookup by id not found for id: " + id);
			} else {
				// Marshal out to a document.
				doc = marshaller.marshalToDom(product);
				// Transform our xsl
				if(!xsl_global.equals(MyResolver.NO_TRANSFORM)) {
					MyResolver resolver = new MyResolver(xsl_global, this.getPrincipal(), this.uriInfo);
					XSLTTransformer trans = XSLTTransformer.getInstance(resolver);
					doc = trans.transform(doc, resolver.getParams());
					type = MediaType.TEXT_HTML_TYPE;
				}
				response = Response.ok().entity(doc).type(type).build();
			}
		} catch (DAOException e) {
			response = Response.serverError().build();
			log.error("DAO exception when accessing data source for product by id.", e);
		} catch (JAXBException e) {
			response = Response.serverError().build();
			log.error("JAXB exception when converting product by id", e);
		} catch (ParserConfigurationException e) {
			response = Response.serverError().build();
			log.error("Parsing error when creating XML document for product by id", e);
		} catch (TransformerException e) {
			response = Response.serverError().build();
			log.error("Error when transforming the xml document.", e);
		} finally {}
		
		return response;
	}
}
