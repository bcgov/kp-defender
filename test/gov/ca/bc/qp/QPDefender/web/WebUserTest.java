package gov.ca.bc.qp.QPDefender.web;

//import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;

import gov.ca.bc.qp.qpcommon.authenticate.QPPrincipal;
import gov.ca.bc.qp.qpcommon.authenticate.UserCredentials;
import gov.ca.bc.qp.qpcommon.authenticate.UserMetaData;
import gov.ca.bc.qp.qpcommon.dom.DefaultResolver;
import gov.ca.bc.qp.qpcommon.marshal.QPMarshaller;
import jakarta.xml.bind.JAXBException;

public class WebUserTest extends gov.ca.bc.qp.QPDefender.test.DataSourceTestUtil {
	/*
	 * this class has been commented out since it has been using Jersey 1.x in case
	 * this class is needed, simply uncomment and convert to apis compatible to
	 * Jersey 2.x
	 */
	// testing variables
	/*
	 * private static final int userid = 4; private static final String
	 * startingCredential = "changes"; private static final String changedCredential
	 * = "changed";
	 * 
	 * @Test public void testUpdateCredentials(){
	 * 
	 * WebUser web = new WebUser(); UserMetaData ud = new
	 * UserMetaData("namespace:name=>value;"); // Set a fake principal usually
	 * handled by the container QPPrincipal principal = new QPPrincipal("changes",
	 * null, ud, userid, 1, 1); web.setPrincipal(principal);
	 * 
	 * // Set our XSLT to none. web.xsl = DefaultResolver.NO_TRANSFORM;
	 * 
	 * QPMarshaller marshaller = new QPMarshaller(); UserCredentials uc_before =
	 * null; UserCredentials uc_after = null;
	 * 
	 *//**
		 * The test is, lookup our usercredentials, update them, ensure they are
		 * changed, update them back ensure they are equal.
		 *//*
			 * try { // Lookup our credentials. uc_before = (UserCredentials)
			 * marshaller.unmarshal((Document)web.getMyInformation(null, null).getEntity(),
			 * new UserCredentials()); // Update them.
			 * web.updateCredentials(changedCredential, changedCredential, null, null); //
			 * Look them up again uc_after = (UserCredentials)
			 * marshaller.unmarshal((Document)web.getMyInformation(null, null).getEntity(),
			 * new UserCredentials()); // ensure they have changed
			 * Assert.assertNotSame(uc_before.getCredential(), uc_after.getCredential()); //
			 * update them back to the way they were
			 * web.updateCredentials(startingCredential, startingCredential, null, null); //
			 * Look them up again uc_after = (UserCredentials)
			 * marshaller.unmarshal((Document)web.getMyInformation(null, null).getEntity(),
			 * new UserCredentials()); // Ensure we're back to the same
			 * Assert.assertTrue(uc_after.isEqual(uc_before));
			 * 
			 * } catch (JAXBException e) { e.printStackTrace(); Assert.fail(); }
			 * 
			 * 
			 * }
			 */
}
