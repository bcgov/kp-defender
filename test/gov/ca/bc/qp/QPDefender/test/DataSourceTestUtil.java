package gov.ca.bc.qp.QPDefender.test;

import gov.ca.bc.qp.qpcommon.connection.QPConnectionPoolSetupTest;
/**
 * Not actually a testing class but a convienience class to be extended by DAO testing classes. It sets up our environment to
 * 	use connection pooling outside of the tomcat container.
 * @author spencer.tickner
 * @see QPConnectionPoolSetupTest.
 */
public class DataSourceTestUtil extends QPConnectionPoolSetupTest {

	@Override
	public String getPassword() {
		return "";
	}

	@Override
	//Add DB Name
	public String getResourceName() {
		return "java:/comp/env/jdbc/";
	}

	@Override
	public String getUsername() {
		return "";
	}

	@Override
	public String getServer() {
		return "";
	}

	@Override
	public String getDBName() {
		return "";
	}
	
	

}
