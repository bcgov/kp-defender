package gov.ca.bc.qp.QPDefender.web;

import javax.ws.rs.core.SecurityContext;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

public class SecurityContextFilter implements ResourceFilter, ContainerRequestFilter {

	SecurityContext context = null;
	
	public SecurityContextFilter(SecurityContext context) {
		this.context = context;
	}
	@Override
	public ContainerRequest filter(ContainerRequest request) {
		request.setSecurityContext(context);
		return request;
	}

	@Override
	public ContainerRequestFilter getRequestFilter() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public ContainerResponseFilter getResponseFilter() {
		// TODO Auto-generated method stub
		return null;
	}

}
