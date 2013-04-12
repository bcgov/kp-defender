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
}
