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
		return "Fcd19eRZi88yG1";
	}

	@Override
	public String getResourceName() {
		return "java:/comp/env/jdbc/QP_Security";
	}

	@Override
	public String getUsername() {
		return "security_user";
	}

	@Override
	public String getServer() {
		return "comp-nxt-dev";
	}

	@Override
	public String getDBName() {
		return "QP_Security";
	}
	
	

}
