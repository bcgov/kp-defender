package gov.ca.bc.qp.QPDefender.DAO;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;
import gov.ca.bc.qp.QPDefender.beans.GroupProduct;
import gov.ca.bc.qp.QPDefender.test.DataSourceTestUtil;
import gov.ca.bc.qp.qpcommon.connection.DAOException;

public class DAOGroupProductTest extends DataSourceTestUtil {

	private final static int groupid1 = 1;

	@Test
	public void testGroupProduct() {
		DAOGroupProduct dao = new DAOGroupProduct();
		try {
			List<GroupProduct> gp1 = dao.lookupGroupProductByGroupId(groupid1);
			Assert.assertFalse(gp1.isEmpty());
		} catch(DAOException e) {
			e.printStackTrace();
			Assert.fail();
		}
		
		
	}
}
