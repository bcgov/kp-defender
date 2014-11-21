package gov.ca.bc.qp.QPDefender.web;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;


/**
 * Interface class for performing searches against Groups, Users and Products.
 * @author spencer.tickner
 *
 */
@Path("/{xsl:.+}/search/")
public class Search {
	
	@PathParam("xsl") String xsl = "";
	
	
	@GET
	@RolesAllowed("qpadmin")
	@Path("/group")
	public Response lookupUGroups(
			@QueryParam("q") String query, @QueryParam("n") String sNum,
			@QueryParam("p") String sPage) {
		
		// Remove null pointers, set defaults
		int iNum = 10; // default 10 results returned
		int iPage = 1; // default to the first page
		
		// If we cannot parse the Strings revert to default.
		try {
			iNum = Integer.parseInt(sNum);
		} catch(Exception ignore) {}
		try {
			iPage = Integer.parseInt(sPage);
		} catch(Exception ignore) {}
		// Remove null pointers
		if(query == null)
			query = "";
		
		
		
		return null;
	}
	

}
