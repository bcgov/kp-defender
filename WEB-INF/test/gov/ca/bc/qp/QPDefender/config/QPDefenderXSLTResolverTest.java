package gov.ca.bc.qp.QPDefender.config;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import gov.ca.bc.qp.qpcommon.authenticate.QPPrincipal;

import org.junit.Test;

public class QPDefenderXSLTResolverTest {

	List<String> roles = null;
	private List<String> getRoles() {
		if(roles == null) {
			roles = new ArrayList<String>();
			roles.add("manager");
			roles.add("student");
		}
		return roles;
	}
	
	@Test
	public void testQPDefenderXSLTResolverNoParams() {
		String unresolvedPath = "test";
		QPPrincipal principal = new QPPrincipal("spencer", this.getRoles(), 1, 1, 1);
		MyResolver resolver = new MyResolver(unresolvedPath, principal, null);

		// Ensure our xslt is resolving
		File f = this.getFile(resolver.getURL());
		Assert.assertTrue(f.exists());
		Assert.assertNotNull(resolver.getSource());
		// Ensure our roles are getting into our params
		Map<String, String> params = resolver.getParams();
		Assert.assertEquals(1, params.size());
		Assert.assertEquals(params.get(MyResolver.ROLE_PARAM_NAME), "manager,student");
		
	}

	@Test
	public void testQPDefenderXSLTResolverWithParams() {
		String unresolvedPath = "test/param1=value1/param2=value2";
		QPPrincipal principal = new QPPrincipal("spencer", this.getRoles(), 1, 1, 1);
		MyResolver resolver = new MyResolver(unresolvedPath, principal, null);

		// Ensure our xslt is resolving
		File f = this.getFile(resolver.getURL());
		Assert.assertTrue(f.exists());
		Assert.assertNotNull(resolver.getSource());
		
		// Ensure our parameters are getting through
		Map<String, String> params = resolver.getParams();
		Assert.assertEquals(3, params.size()); // Should have one more than passed in because of roles.
		Assert.assertEquals("value1", params.get("param1"));
		Assert.assertEquals("value2", params.get("param2"));
		
	}
	
	private File getFile(URL url) {
		File f = null;
		try {
			f = new File(url.toURI());
		} catch (URISyntaxException e) {
			f = new File(url.getPath());
		}
		return f;
	}
}
