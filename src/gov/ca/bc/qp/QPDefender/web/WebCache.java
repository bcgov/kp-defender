package gov.ca.bc.qp.QPDefender.web;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import gov.ca.bc.qp.qpcommon.code.QPCache;

@Path("cache")
public class WebCache {

	@GET
	@Path("clear")
	@RolesAllowed("qpadmin")
	public Response clearCache() {
		QPCache.resetAllCaches();
		return Response.ok().entity("Cache Cleared").type(MediaType.TEXT_HTML).build();
	}
}
