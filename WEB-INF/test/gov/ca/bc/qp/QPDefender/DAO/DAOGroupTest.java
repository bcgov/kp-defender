package gov.ca.bc.qp.QPDefender.DAO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;
import gov.ca.bc.qp.QPDefender.beans.Group;
import gov.ca.bc.qp.QPDefender.beans.GroupProduct;
import gov.ca.bc.qp.QPDefender.beans.ProductAccess;
import gov.ca.bc.qp.QPDefender.beans.UserAccess;
import gov.ca.bc.qp.QPDefender.test.DataSourceTestUtil;
import gov.ca.bc.qp.QPDefender.utility.ObjectUtil;
import gov.ca.bc.qp.qpcommon.authenticate.DAOUser;
import gov.ca.bc.qp.qpcommon.authenticate.User;
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
	
	@Test
	public void testAddGroup() {
		DAOUser uDao = new DAOUser();
		User user = null;
		try {
			user = uDao.lookupUserById(1);
		} catch (ObjectNotFoundException e) {
			e.printStackTrace();
			Assert.fail();
		} catch (DAOException e) {
			e.printStackTrace();
			Assert.fail();
		}
		
		Group group = new Group(-1,false,1,"company","dept","addr","addr2", "Victoria", "BC",
				"Canada", "V9A5E4", "(250)123-1234", "", "spencertickner@gmail.com", "Spencer", "",
				"spencertickner@gmail.com", new Date(), new Date(), new Date(), user, new Date(),
				user, "This is a customer note", "package", false, 33, "government", null, "1234", 
				"Craig", new ArrayList<UserAccess>(), new ArrayList<GroupProduct>());
		
		DAOGroup dao = new DAOGroup();
		
	
		Group copy = null;
		int id = -1;
		try {
			 id = dao.addGroup(group);
			 copy = dao.lookupGroup(id);
		} catch (DAOException e) {
			e.printStackTrace();
			Assert.fail();
		} catch (ObjectNotFoundException e) {
			e.printStackTrace();
			Assert.fail();
		} finally {
			try {
				dao.deleteGroup(id);
			} catch(Exception ex) {
				System.out.println("Delete failed, WARNING: database may have test data within it.");
				Assert.fail();
			}
		}
		// Have to add our id to the group we added.
		group.setId(id);
		//System.out.println(ObjectUtil.equal(group.getStart_dt(), copy.getStart_dt()));
		Assert.assertTrue(group.isEqual(copy));
		
	}
	
	@Test
	public void testProductExistsForGroup() {
		int groupid = 1;
		int productid_good = 1;
		int productid_bad = 122222;
		DAOGroup dao = new DAOGroup();
		try {
			Assert.assertTrue(dao.isProductAssociatedToGroup(productid_good, groupid));
			Assert.assertFalse(dao.isProductAssociatedToGroup(productid_bad, groupid));
		} catch(Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
