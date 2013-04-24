package gov.ca.bc.qp.QPDefender.DAO;

import junit.framework.Assert;

import org.junit.Test;

import gov.ca.bc.qp.QPDefender.beans.UnknownCredentialException;
import gov.ca.bc.qp.QPDefender.beans.UserAccess;
import gov.ca.bc.qp.QPDefender.test.DataSourceTestUtil;
import gov.ca.bc.qp.qpcommon.connection.DAOException;

public class DAOAccessTest extends DataSourceTestUtil {

	// static testing userid
	private static final int userid1 = 1;
	private static final int userid2 = 2;
	
	@Test
	public void testDAOAccess_User() {
		DAOAccess dao = new DAOAccess();
		UserAccess ua1 = null;
		UserAccess ua1_copy = null;
		UserAccess ua2 = null;
		UserAccess ua_empty = new UserAccess();
		try {
			ua1 = dao.lookupUserAccess(userid1);
			ua1_copy = dao.lookupUserAccess(userid1);
			ua2 = dao.lookupUserAccess(userid2);
		} catch (DAOException e) {
			e.printStackTrace();
			Assert.fail();
		} catch (UnknownCredentialException e) {
			e.printStackTrace();
			Assert.fail();
		}
		
		Assert.assertTrue(ua1.isEqual(ua1_copy));
		Assert.assertFalse(ua1.isEqual(ua2));
		Assert.assertTrue(ua_empty.isEmpty());	
	}
	
	@Test
	public void testDAOAddAccess_User() {
		DAOAccess dao = new DAOAccess();
		UserAccess ua1 = null;
		UserAccess copy = null;
		try {
			// get a copy so we can manipulate one, ensure changes are made, and revert at the end to keep db
			//	static.
			ua1 = dao.lookupUserAccess(userid1);
			copy = dao.lookupUserAccess(userid1);
			
			int dummyTimeout = 1009;
			// we'll change a product access timeout.
			ua1.getProductAccess().get(0).setTimeout(dummyTimeout);
			dao.AddUpdateProductAccess(ua1);
			
			// Now we'll re-look it up to ensure it's changed.
			ua1 = dao.lookupUserAccess(userid1);
			Assert.assertEquals(dummyTimeout, ua1.getProductAccess().get(0).getTimeout());
			
			// Now we'll try chaning some user access information and insure that changes as well.
			String dummyCredential = "DUMMY_DATA";
			ua1.setCredential("DUMMY_DATA");
			dao.AddUpdateUserAccess(ua1);
			// Relook it up to ensure the changes took affect.
			ua1 = dao.lookupUserAccess(userid1);
			Assert.assertEquals(dummyCredential, ua1.getCredential());
			

			
		} catch (DAOException e) {
			e.printStackTrace();
			Assert.fail();
		} catch (UnknownCredentialException e) {
			e.printStackTrace();
			Assert.fail();
		} catch (InvalidCharacterException e) {
			e.printStackTrace();
			Assert.fail();
		} finally {
			// Ok we change a product Access and a User Access all for now, revert it back to the copy.
			try {
				dao.AddUpdateUserAccess(copy);
				dao.AddUpdateProductAccess(copy);
			} catch(Exception e) {
				System.out.println("Warning Database may be out of sync. Clean up failed.");
				e.printStackTrace();
				Assert.fail();
			}
		}
	}
	
}
