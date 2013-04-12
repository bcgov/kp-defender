package gov.ca.bc.qp.QPDefender.DAO;

import junit.framework.Assert;
import gov.ca.bc.qp.QPDefender.beans.Group;
import gov.ca.bc.qp.QPDefender.test.DataSourceTestUtil;
import gov.ca.bc.qp.qpcommon.code.ObjectNotFoundException;
import gov.ca.bc.qp.qpcommon.connection.DAOException;

import org.junit.Test;

public class DAOGroupTest extends DataSourceTestUtil  {

	// Group in our database for testing purposes.
	private static final int groupid1 = 1;
	private static final int groupid2 = 2;
	
	@Test
	public void testDAOGroup() {
		DAOGroup dao = new DAOGroup();
		Group group1 = null;
		Group group2 = null;
		Group group1_copy = null;
		Group group_empty = new Group();
		try {
			group1 = dao.lookupGroup(groupid1);
			group1_copy = dao.lookupGroup(groupid1);
			group2 = dao.lookupGroup(groupid2);
		} catch (ObjectNotFoundException e) {
			e.printStackTrace();
			Assert.fail();
		} catch (DAOException e) {
			e.printStackTrace();
			Assert.fail();
		}
		
		Assert.assertTrue(group1.isEqual(group1_copy));
		Assert.assertFalse(group1.isEqual(group2));
		Assert.assertTrue(group_empty.isEmpty());
	}
}
