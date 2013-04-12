package gov.ca.bc.qp.QPDefender.DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import gov.ca.bc.qp.QPDefender.beans.ProductAccess;
import gov.ca.bc.qp.QPDefender.beans.UnknownCredentialException;
import gov.ca.bc.qp.QPDefender.beans.UserAccess;
import gov.ca.bc.qp.qpcommon.authenticate.DAOProducts;
import gov.ca.bc.qp.qpcommon.authenticate.DAORoles;
import gov.ca.bc.qp.qpcommon.authenticate.DAOUser;
import gov.ca.bc.qp.qpcommon.authenticate.Product;
import gov.ca.bc.qp.qpcommon.authenticate.Role;
import gov.ca.bc.qp.qpcommon.authenticate.User;
import gov.ca.bc.qp.qpcommon.code.ObjectNotFoundException;
import gov.ca.bc.qp.qpcommon.connection.DAOException;
import gov.ca.bc.qp.qpcommon.connection.DAOSecurity;

/**
 * The Data Access Object for compiling information based on a users access to various systems.
 * @author spencer.tickner
 */
public class DAOAccess extends DAOSecurity {

	/**
	 * Looks up access information for a user.
	 * @param userid Unique identifier for the user.
	 * @return Access information for this user.
	 * @throws DAOException An error occurred while accessing our data source.
	 * @throws UnknownCredentialException The user does not have any credentials set.
	 */
	public UserAccess lookupUserAccess(int userid) throws DAOException, UnknownCredentialException {
		Connection con = null;
		CallableStatement stmt = null;
		ResultSet rs = null;
		UserAccess access = null;
		DAOUser daoUser = new DAOUser();
		try {
			User user = new User();
			try {
				// Lookup our user
				user = daoUser.lookupUserById(userid);
			} catch (ObjectNotFoundException e) {
				// Ignore not found objects, just send back an empty user.
			}
			// Get our list of Products and access information for this user.
			List<ProductAccess> pas = this.lookupProductAccess(userid);
			con = this.getConnectionPool().getConnection();
			// Call our stored procedure.
			stmt = con.prepareCall("{call LookupCredentialByUser(" + Integer.toString(userid) + ")}");
			// Retrieve result set.
			rs = stmt.executeQuery();
			if(rs.next()) {
				access = new UserAccess(user, pas, rs.getString("type"), rs.getString("Credential"), rs.getString("Credential2"));
			} else {
				throw new UnknownCredentialException("The user " + userid + " has no credentials set");
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			// Cleanup
			try { this.getConnectionPool().closeConnection(con); } catch(Exception ignore) {}
			try { stmt.close(); } catch(Exception ignore) {}
			try { rs.close(); } catch(Exception ignore) {}
		}
			
		return access;
	}
	
	/**
	 * Looks up access information for a product.
	 * @param userid Unique identifier for a user.
	 * @return Access information for a product.
	 * @throws DAOException an error occurred when accessing the data source.
	 */
	public List<ProductAccess> lookupProductAccess(int userid) throws DAOException {
		List<ProductAccess> access = new ArrayList<ProductAccess>();
		Connection con = null;
		CallableStatement stmt = null;
		ResultSet rs = null;
		DAORoles daoRole = new DAORoles();
		try {
			con = this.getConnectionPool().getConnection();
			// Call our stored procedure.
			stmt = con.prepareCall("{call LookupProductAccessByUser(" + Integer.toString(userid) + ")}");
			// Retrieve result set.
			rs = stmt.executeQuery();
			while(rs.next()) {
			
				int pId = rs.getInt("productid");
				List<Role> roles = daoRole.getRolesByUserAndProduct(userid, pId);
				Product p = new Product();
				try {
					p = Product.getProductById(pId);
				} catch (ObjectNotFoundException e) {
					// Ignore it, allow an empty product to be added.
				}
				ProductAccess pa = new ProductAccess(
					rs.getInt("userid"), p, roles, rs.getInt("timeout"), rs.getBoolean("active"));
				access.add(pa);
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			try { this.getConnectionPool().closeConnection(con); } catch(Exception ignore) {}
			try { stmt.close(); } catch(Exception ignore) {}
			try { rs.close(); } catch(Exception ignore) {}
		}
		
		return access;
	}
}
