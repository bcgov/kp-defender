package gov.ca.bc.qp.QPDefender.web;

import javax.ws.rs.core.Response;

import junit.framework.Assert;
import gov.ca.bc.qp.QPDefender.DAO.DAOAccess;
import gov.ca.bc.qp.QPDefender.beans.UnknownCredentialException;
import gov.ca.bc.qp.qpcommon.authenticate.UserAccess;
import gov.ca.bc.qp.qpcommon.connection.DAOException;

import org.junit.Test;

public class WebUserAccessTest extends gov.ca.bc.qp.QPDefender.test.DataSourceTestUtil {

	private static final int id = 1055;
	@Test
	public void testAddUpdateUserAccess() {
		DAOAccess dao = new DAOAccess();
		WebUserAccess rest = new WebUserAccess();
		UserAccess ua = null;
		try {
			ua = dao.lookupUserAccess(id);
		} catch (DAOException e) {
			e.printStackTrace();
			Assert.fail();
		} catch (UnknownCredentialException e) {
			e.printStackTrace();
			Assert.fail();
		}
		String old_password = ua.getCredential();
		ua.setCredential("changed_password");
		
		Response response = rest.addUserAccess(ua);
		Assert.assertEquals(response.getStatus(), 200);
		
		try {
			ua = dao.lookupUserAccess(id);
			Assert.assertNotSame(ua.getCredential(), old_password);

		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		} catch (UnknownCredentialException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		} finally {
			try {
				ua.setCredential(old_password);
				response = rest.addUserAccess(ua);
				Assert.assertEquals(response.getStatus(), 200);
			} catch(Exception e) {
				System.out.println("Warning, unable to revert user, database may be out of sync.");
				Assert.fail();
			}
		}
		
		
	}
}
