package gov.ca.bc.qp.QPDefender.DAO;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gov.ca.bc.qp.QPDefender.beans.UnknownCredentialException;
import gov.ca.bc.qp.qpcommon.authenticate.DAORoles;
import gov.ca.bc.qp.qpcommon.authenticate.DAOUser;
import gov.ca.bc.qp.qpcommon.authenticate.IPAuthenticator;
import gov.ca.bc.qp.qpcommon.authenticate.PasswordHash;
import gov.ca.bc.qp.qpcommon.authenticate.Product;
import gov.ca.bc.qp.qpcommon.authenticate.ProductAccess;
import gov.ca.bc.qp.qpcommon.authenticate.Role;
import gov.ca.bc.qp.qpcommon.authenticate.User;
import gov.ca.bc.qp.qpcommon.authenticate.UserAccess;
import gov.ca.bc.qp.qpcommon.authenticate.UserCredentials;
import gov.ca.bc.qp.qpcommon.code.ObjectNotFoundException;
import gov.ca.bc.qp.qpcommon.connection.DAOException;
import gov.ca.bc.qp.qpcommon.connection.DAOSecurity;

/**
 * The Data Access Object for compiling information based on a users access to various systems.
 * @author spencer.tickner
 */
public class DAOAccess extends DAOSecurity {
	Logger log = LogManager.getLogger(this.getClass());

	/**
	 * Deletes a user and all the access that is assigned to them.
	 * @param userid Unique identifier for this user.
	 * @throws DAOException An error occurred when accessing our data source.
	 */
	public void DeleteUserAndAccessById(int userid) throws DAOException {
		Connection con = null;
		CallableStatement stmt = null;
		try {
			con = this.getConnectionPool().getConnection();
			stmt = con.prepareCall("{call DeleteUserById(?)}");
			stmt.setInt(1, userid);
			stmt.execute();
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			try { this.getConnectionPool().closeConnection(con); } catch(Exception ignore) {}
			try { stmt.close(); } catch(Exception ignore) {}			
		}
	}
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
			// Don't auto commmit in case something goes wrong while ading Product Access.
			con.setAutoCommit(false);
			stmt = con.prepareCall("{? = call UpdateAddUserAccess(?,?,?,?,?,?,?,?,?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setInt(2, access.getUser().getId());
			stmt.setInt(3, access.getUser().getGroupId());
			stmt.setString(4, access.getUser().getUsername());
			stmt.setString(5, access.getUser().getEmail());
			stmt.setInt(6, access.getUserCredentialId());
			stmt.setString(7, access.getCredentialType());
			
			// Because we hash our credentials in the database we must determine if this is a password credential
			//	and if so whether or not it's hashed.. If it isn't hash it before writing it to disk.
			String hashedCred = access.getCredential();
			if(access.getCredentialType().equals(UserCredentials.CredType.STANDARD.toString())) {
				if(!PasswordHash.isHashed(access.getCredential()))
					hashedCred = PasswordHash.createHash(access.getCredential());
			}
			
			stmt.setString(8, hashedCred);
			stmt.setString(9, access.getCredential2());
			stmt.setString(10, access.getUser().getMeta());
			stmt.execute();
			
			// we will always get the userid out of our procedure in case this is a new user we'd be adding product
			//		access information to userid -1. 
			int userid = stmt.getInt(1);
			if(access.getProductAccess() != null)
				this.AddUpdateProductAccess(access.getUser().getGroupId(), userid, access);
			// Everythings gone well, commit away.
			con.commit();
			
			// Now if the credentials added where of the IP type reset our IPAuthentication cache.
			if(!access.getCredentialType().equalsIgnoreCase(UserCredentials.CredType.STANDARD.toString())) {
				log.info("User's Access has been added or modified. Updating IPAuthenticator cache to reflect changes.");
				IPAuthenticator.reset();
			}
		} catch (SQLException e) {
			try { 
				con.rollback(); 
			} catch(Exception ex) {
				log.error("Could not rollback AddUpdateUserAccess. " + access.getUserCredentialId(), ex);
			}
			throw new DAOException(e);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	public void AddUpdateProductAccess(int groupid, int userid, ProductAccess pa) throws DAOException, InvalidCharacterException {
		Connection con = null;
		CallableStatement stmt = null;
		try {
			con = this.getConnectionPool().getConnection();
			stmt = con.prepareCall("{call UpdateAddProductAccess(?,?,?,?,?,?,?)}");
			stmt.setInt(1, groupid);
			stmt.setInt(2, pa.getUserProductsID());
			stmt.setInt(3, userid);
			stmt.setInt(4, pa.getProduct().getId());
			stmt.setInt(5, pa.getTimeout());
			stmt.setBoolean(6, pa.isActive());
			stmt.setString(7, this.createRoleCSS(pa));
			stmt.execute();
			// Reset our IPAuthenticator Cache to reflect changes.
			log.info("Product Access has been added or modified. Updating IPAuthenticator cache to reflect changes.");
			IPAuthenticator.reset();
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			// clean up our connection objects
			try { this.getConnectionPool().closeConnection(con); } catch(Exception ignore) {}
			try { stmt.close(); } catch(Exception ignore) {}			
		}
	}
	
	/**
	 * Adds or updates a users access to a group product.
	 * @param userProductId	The unique identifier for this users access to this group product. If not found will add, else update.
	 * @param userid The unique identifier for the user accessing this product.
	 * @param groupProductId Unique identifier for a groups access to a product that this user belongs to.
	 * @param timeout 
	 * @param active
	 * @return unique identifier for this users access to a product.
	 * @throws DAOException
	 */
	public int AddUpdateUserProduct(int userProductId, int userid, int groupProductId, int timeout, boolean active) throws DAOException {
		Connection con = null;
		CallableStatement stmt = null;
		int id = -1;
		try {
			con = this.getConnectionPool().getConnection();
			stmt = con.prepareCall("{call UpdateAddUserProduct(?,?,?,?,?,?)}");
			stmt.setInt(1, userProductId);
			stmt.setInt(2, userid);
			stmt.setInt(3, groupProductId);
			stmt.setInt(4, timeout);
			stmt.setBoolean(5, active);
			stmt.registerOutParameter(6, java.sql.Types.INTEGER);
			stmt.execute();
			id = stmt.getInt(6);
			// Reset our IPAuthenticator Cache to reflect changes.
			log.info("Product Access has been added or modified. Updating IPAuthenticator cache to reflect changes.");
			IPAuthenticator.reset();
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			// clean up our connection objects
			try { this.getConnectionPool().closeConnection(con); } catch(Exception ignore) {}
			try { stmt.close(); } catch(Exception ignore) {}			
		}
		return id;
	}

	
	/**
	 * Looks up the access to a product by that accesses unique identifier.
	 * @param productAccessId	The unique identifier for a users access to a product.
	 * @throws DAOException	Error occurred when accessing our datasource.
	 */
	public ProductAccess lookupProductAccessById(int productAccessId) throws DAOException {
		Connection con = null;
		CallableStatement stmt = null;
		ResultSet rs = null;
		ProductAccess pa = null;
		try {
			con = this.getConnectionPool().getConnection();
			stmt = con.prepareCall("{call lookupProductAccessById(?)}");
			stmt.setInt(1, productAccessId);
			rs = stmt.executeQuery();
			if(rs.next()) {
				pa = this.createProductAccess(rs.getInt("ID"), 
						rs.getInt("productId"), rs.getInt("userid"), 
						rs.getInt("timeout"), rs.getBoolean("active"));
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			// Cleanup
			try { this.getConnectionPool().closeConnection(con); } catch(Exception ignore) {}
			try { stmt.close(); } catch(Exception ignore) {}
			try { rs.close(); } catch(Exception ignore) {}			
		}
		return pa;
	}
	
	/**
	 * Adds, or updates, in bulk all the Product Access constraints for a user.
	 * @param access	Object representing all the products and access constraints for a user.
	 * @throws DAOException	Error occurred when accessing our data source.
	 * @throws InvalidCharacterException	An invalid character was found in one of the components.
	 */
	public void AddUpdateProductAccess(int groupid, int userid, UserAccess access) throws DAOException, InvalidCharacterException {
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
			stmt = con.prepareCall("{call UpdateAddProductAccess(?,?,?,?,?,?,?)}");
			for(int i = 0; i < access.getProductAccess().size(); i++) {
				ProductAccess pa = access.getProductAccess().get(i);
				
				stmt.setInt(1, groupid);
				stmt.setInt(2, pa.getUserProductsID());
				stmt.setInt(3, userid); // Always used passed in value in case the UserAccess Bean is new and everything has userid of -1.
				stmt.setInt(4, pa.getProduct().getId());
				stmt.setInt(5, pa.getTimeout());
				stmt.setBoolean(6, pa.isActive());
				stmt.setString(7, this.createRoleCSS(pa));
				stmt.execute();
			} 
			// Reset our IPAuthenticator Cache to reflect changes.
			log.info("Product Access has been added or modified. Updating IPAuthenticator cache to reflect changes.");
			IPAuthenticator.reset();

		}catch(DAOException e) {
			//try { con.rollback(); } catch(Exception ignore) { log.error("Unable to roll back ProductAccess addition", ignore); }
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
	 * Deletes a users access to a product as well as it's associated role information.
	 * @param productAccessId Unique identifier of the access to a product to delete.
	 * @throws DAOException Error occurred while accessing our datasource.
	 */
	public void deleteProductAccess(int productAccessId) throws DAOException {
		Connection con = null;
		CallableStatement stmt = null;
		
		try {
			con = this.getConnectionPool().getConnection();
			stmt = con.prepareCall("{call deleteProductAccess(?)}");
			stmt.setInt(1, productAccessId);
			stmt.execute();
			// Reset our IPAuthenticator Cache to reflect changes.
			log.info("Product Access has been deleted. Updating IPAuthenticator cache to reflect changes.");
			IPAuthenticator.reset();
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			// Cleanup
			try { this.getConnectionPool().closeConnection(con); } catch(Exception ignore) {}
			try { stmt.close(); } catch(Exception ignore) {}			
		}
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
			
				access.add(this.createProductAccess(
						rs.getInt("ID"), rs.getInt("productid"), rs.getInt("userid"), 
						rs.getInt("timeout"), rs.getBoolean("active")));
				/*
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
				*/
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
	
	/**
	 * Convenience method for calling the various objects to create a fully instantiated product access object.
	 * @param id		Unique identifier for the access to this product for a certain user.
	 * @param productId	Unique identifier for the product that this user has access to.
	 * @param userid	Unique identifier for the user that has access to this product.
	 * @param timeout	The time (in minutes) before the user is kicked out of this product.
	 * @param active	Whether or not the user can currently access this product.
	 * @return			Complete object representing a user and the access to a products details.
	 * @throws DAOException Error occurred while accessing our datasource.
	 */
	private ProductAccess createProductAccess(int id, int productId, int userid, int timeout, boolean active) throws DAOException {
		DAORoles daoRole = new DAORoles();
		Product product = null;
		List<Role> roles = null;
		try {
			product = Product.getProductById(productId);
			roles = daoRole.getRolesByUserAndProduct(userid, productId);
		} catch (ObjectNotFoundException e) {
			// If a product is not found just add empty product.
			product = new Product();
		}
		
		return new ProductAccess(id, userid, product, roles, timeout, active);
	}
}
