package gov.ca.bc.qp.QPDefender.DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

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
	Logger log = Logger.getLogger(this.getClass());

	/**
	 * Adds or updates a user and it's credential components.
	 * @param access	A User and all their associated access information.
	 * @throws DAOException Error occurred while accessing our data source.
	 * @throws InvalidCharacterException 
	 */
	public void AddUpdateUserAccess(UserAccess access) throws DAOException, InvalidCharacterException {
		Connection con = null;
		CallableStatement stmt = null;
		try {
			con = this.getConnectionPool().getConnection();
			con.setAutoCommit(false);
			stmt = con.prepareCall("{call UpdateAddUserAccess(?,?,?,?,?,?,?,?)}");
			stmt.setInt(1, access.getUser().getId());
			stmt.setInt(2, access.getUser().getGroupId());
			stmt.setString(3, access.getUser().getUsername());
			stmt.setString(4, access.getUser().getEmail());
			stmt.setInt(5, access.getUserCredentialId());
			stmt.setString(6, access.getCredentialType());
			stmt.setString(7, access.getCredential());
			stmt.setString(8, access.getCredential2());
			stmt.execute();
			
			if(access.getProductAccess() != null)
				this.AddUpdateProductAccess(access);
			
			con.commit();
		} catch (SQLException e) {
			try { 
				con.rollback(); 
			} catch(Exception ex) {
				log.error("Could not rollback AddUpdateUserAccess. " + access.getUserCredentialId(), ex);
			}
			throw new DAOException(e);
		} finally {
			try { this.getConnectionPool().closeConnection(con); } catch(Exception ignore) {}
			try { stmt.close(); } catch(Exception ignore) {}
		}
		
	}
	
	/**
	 * Adds a single Product Access information for a user.
	 * @param userid	The user that is associated with the details of access to this product.
	 * @param pa		The Product this user has access to and it's various access constraints.
	 * @throws DAOException	Error occurred while communicating with the database.
	 * @throws InvalidCharacterException An invalid character was found in one of the components.
	 */
	public void AddUpdateProductAccess(int userid, ProductAccess pa) throws DAOException, InvalidCharacterException {
		Connection con = null;
		CallableStatement stmt = null;
		try {
			con = this.getConnectionPool().getConnection();
			stmt = con.prepareCall("{call UpdateAddProductAccess(?,?,?,?,?,?)}");
			stmt.setInt(1, pa.getUserProductsID());
			stmt.setInt(2, userid);
			stmt.setInt(3, pa.getProduct().getId());
			stmt.setInt(4, pa.getTimeout());
			stmt.setBoolean(5, pa.isActive());
			stmt.setString(6, this.createRoleCSS(pa));
			stmt.execute();
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			// clean up our connection objects
			try { this.getConnectionPool().closeConnection(con); } catch(Exception ignore) {}
			try { stmt.close(); } catch(Exception ignore) {}			
		}
	}
	
	/**
	 * Adds, or updates, in bulk all the Product Access constraints for a user.
	 * @param access	Object representing all the products and access constraints for a user.
	 * @throws DAOException	Error occurred when accessing our data source.
	 * @throws InvalidCharacterException	An invalid character was found in one of the components.
	 */
	public void AddUpdateProductAccess(UserAccess access) throws DAOException, InvalidCharacterException {
		Connection con = null;
		CallableStatement stmt = null;
		boolean complete = true; // To manage roll backs and commits.
		try {
			con = this.getConnectionPool().getConnection();
			// Because we delete all user access info and then re-add it, make 
			//  sure we can roll back if anything goes wrong.
			con.setAutoCommit(false); 
			
			// Fist delete all our product access information for this user.
			//stmt = con.prepareCall("{call DeleteProductAccessByUserid(?)}");
			//stmt.setInt(1, access.getUser().getId());
			//stmt.execute();
			
			// Add all the new information, essentially completing an update.
			stmt = con.prepareCall("{call UpdateAddProductAccess(?,?,?,?,?,?)}");
			for(int i = 0; i < access.getProductAccess().size(); i++) {
				ProductAccess pa = access.getProductAccess().get(i);
				
				stmt.setInt(1, pa.getUserProductsID());
				stmt.setInt(2, access.getUser().getId());
				stmt.setInt(3, pa.getProduct().getId());
				stmt.setInt(4, pa.getTimeout());
				stmt.setBoolean(5, pa.isActive());
				stmt.setString(6, this.createRoleCSS(pa));
				stmt.execute();
			} 

		}catch(DAOException e) {
			try { con.rollback(); } catch(Exception ignore) { log.error("Unable to roll back ProductAccess addition", ignore); }
			complete = false;
			throw e;
		} catch (InvalidCharacterException e) {
			try { con.rollback(); } catch(Exception ignore) { log.error("Unable to roll back ProductAccess addition", ignore); }
			complete = false;
			throw e;
		} catch (SQLException e) {
			try { con.rollback(); } catch(Exception ignore) { log.error("Unable to roll back ProductAccess addition", ignore); }
			complete = false;
			throw new DAOException(e);
		} finally {
			// If everything went well commit the transaction
			if(complete) {
				try { con.commit(); } catch(Exception ignore) { log.error("Product Access commit failed", ignore); }
			}
			// clean up our connection objects
			try { this.getConnectionPool().closeConnection(con); } catch(Exception ignore) {}
			try { stmt.close(); } catch(Exception ignore) {}
		}
	}
	
	/**
	 * Creates a comma seperated list of Role Names for passing to the database.
	 * @param pa Product and users associated access information.
	 * @return	Comma seperated string on rolenames that this user has access to for this product.
	 * @throws InvalidCharacterException One of the rolenames contains a comma which is not allowed.
	 */
	private String createRoleCSS(ProductAccess pa) throws InvalidCharacterException {
		// We create a string of rolenames seperated by commas to pass to the database.
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < pa.getRoles().size(); i++) {
			// Roles can't have commas, enforce it.
			if(pa.getRoles().get(i).getRoleName().contains(",")) {
				throw new InvalidCharacterException("Exception when trying to Add Product Access information. Rolenames cannot contain commas.");
			} else {
				sb.append(pa.getRoles().get(i).getRoleName());
				sb.append(",");
			}
		}
		String rolenames = sb.toString();
		// Kill the trailing commma
		if(rolenames != null && rolenames.length() > 1)
			rolenames = rolenames.substring(0, rolenames.length() - 1);
		return rolenames;
		
	}

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
				access = new UserAccess(user, pas, rs.getInt("ID"), rs.getString("type"), rs.getString("Credential"), rs.getString("Credential2"));
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
				ProductAccess pa = new ProductAccess( rs.getInt("ID"),
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
