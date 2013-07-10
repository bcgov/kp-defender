package gov.ca.bc.qp.QPDefender.config;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import gov.ca.bc.qp.qpcommon.authenticate.QPPrincipal;
import gov.ca.bc.qp.qpcommon.authenticate.UserMetaData;
import gov.ca.bc.qp.qpcommon.dom.DefaultResolver;
import gov.ca.bc.qp.qpcommon.dom.XSLTResolver;

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
		// Placeholder for this test.
		UserMetaData ud = new UserMetaData("namespace:name=>value");
		QPPrincipal principal = new QPPrincipal("spencer", this.getRoles(), ud, 1, 1, 1);
		XSLTResolver resolver = new DefaultResolver(unresolvedPath, principal, null, this);

		// Ensure our xslt is resolving
		File f = this.getFile(resolver.getURL());
		Assert.assertTrue(f.exists());
		Assert.assertNotNull(resolver.getSource());
		// Ensure our roles are getting into our params
		Map<String, Object> params = resolver.getParams();
		Assert.assertEquals(1, params.size());
		Assert.assertEquals(params.get(DefaultResolver.ROLE_PARAM_NAME), "manager,student");
		
	}

	@Test
	public void testQPDefenderXSLTResolverWithParams() {
		String unresolvedPath = "test/param1=value1/param2=value2";
		// Placeholder for this test.
		UserMetaData ud = new UserMetaData("namespace:name=>value");
		QPPrincipal principal = new QPPrincipal("spencer", this.getRoles(), ud, 1, 1, 1);
		XSLTResolver resolver = new DefaultResolver(unresolvedPath, principal, null, this);

		// Ensure our xslt is resolving
		File f = this.getFile(resolver.getURL());
		Assert.assertTrue(f.exists());
		Assert.assertNotNull(resolver.getSource());
		
		// Ensure our parameters are getting through
		Map<String, Object> params = resolver.getParams();
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
