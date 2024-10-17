package gov.ca.bc.qp.QPDefender.web;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.SecurityContext;

import org.glassfish.jersey.server.ContainerRequest;

//import com.sun.jersey.spi.container.ContainerRequest;
//import com.sun.jersey.spi.container.ContainerRequestFilter;
//import com.sun.jersey.spi.container.ContainerResponseFilter;
//import com.sun.jersey.spi.container.ResourceFilter;

//public class SecurityContextFilter implements ResourceFilter, ContainerRequestFilter {
public class SecurityContextFilter implements DynamicFeature, ContainerRequestFilter {

	SecurityContext context = null;
	
	public SecurityContextFilter(SecurityContext context) {
		this.context = context;
	}
	public ContainerRequest filter(ContainerRequest request) {
		request.setSecurityContext(context);
		return request;
	}

	public ContainerRequestFilter getRequestFilter() {
		// TODO Auto-generated method stub
		return this;
	}

	public ContainerResponseFilter getResponseFilter() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void configure(ResourceInfo resourceInfo, FeatureContext context) {
		// TODO Auto-generated method stub
		
	}

}
