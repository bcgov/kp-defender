/*
 * Copyright (c) 2013, Queen's Printer of British Columbia, Canada and/or its affiliates. 
 * All rights reserved. DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE Header.
 * 
 * Please contact Queen's Printer of British Columbia, PO Box 9452 Stn Prov Govt, Victoria 
 * BC, V8W 9V7, (250) 387-3309 if you have any questions or have received this class in 
 * error.
 * 
 */
package gov.ca.bc.qp.QPDefender.DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import gov.ca.bc.qp.QPDefender.beans.Group;
import gov.ca.bc.qp.QPDefender.beans.GroupProduct;
import gov.ca.bc.qp.QPDefender.beans.UnknownCredentialException;
import gov.ca.bc.qp.qpcommon.authenticate.UserAccess;
import gov.ca.bc.qp.qpcommon.authenticate.DAOUser;
import gov.ca.bc.qp.qpcommon.authenticate.User;
import gov.ca.bc.qp.qpcommon.code.ObjectNotFoundException;
import gov.ca.bc.qp.qpcommon.connection.DAOException;
import gov.ca.bc.qp.qpcommon.connection.DAOSecurity;

/**
 * Data access object for accessing data source information on Groups.
 * @author spencer.tickner
 *
 */
public class DAOGroup extends DAOSecurity {

	/**
	 * 
	 * @param groupProduct
	 * @throws DAOException
	 */
	public void AddProductToGroup(GroupProduct groupProduct) throws DAOException {
		Connection con = null;
		CallableStatement stmt = null;
		try {
			con = this.getConnectionPool().getConnection();
			stmt = con.prepareCall("{call UpdateAddProductToGroup(?,?,?,?,?)}");
			stmt.setInt(1, groupProduct.getId());
			stmt.setInt(2, groupProduct.getGroupid());
			stmt.setInt(3, groupProduct.getProduct().getId());
			stmt.setInt(4, groupProduct.getConcurrent());
			stmt.setTimestamp(5, new Timestamp(groupProduct.getExpiryDate().getTime()));
			stmt.execute();
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			try { this.getConnectionPool().closeConnection(con); } catch(Exception ignore) {}
			try { stmt.close(); } catch(Exception ignore) {}			
		}
	}
	/**
	 * Looks up a group based on a unique identifier.
	 * @param groupId Unique identifier for a group.
	 * @return	All a groups associated information.
	 * @throws ObjectNotFoundException	The group was not found.
	 * @throws DAOException	An error occurred when accessing our data source.
	 */
	public Group lookupGroup(int groupId) throws ObjectNotFoundException, DAOException {
		Group group = null;
		Connection con = null;
		CallableStatement stmt = null;
		ResultSet rs = null;

		try {
			// Next we'll lookup the group infomation.
			con = this.getConnectionPool().getConnection();
			// Call our stored procedure.
			stmt = con.prepareCall("{call LookupGroupById(" + Integer.toString(groupId) + ")}");
			// Retrieve result set.
			rs = stmt.executeQuery();
			if(rs.next()) {
				group = this.populateGroup(rs);
			} else {
				throw new ObjectNotFoundException("No Group with the id " + Integer.toString(groupId) + " was found");
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} catch (UnknownCredentialException e) {
			throw new DAOException(e);
		} finally {
			try { this.getConnectionPool().closeConnection(con); } catch(Exception ignore) {}
			try { stmt.close(); } catch(Exception ignore) {}
			try { rs.close(); } catch(Exception ignore) {}
		}
		return group;
	}
	
	public void updateGroup(Group group) throws DAOException {
		Connection con = null;
		CallableStatement stmt = null;
		try {
			// Ok, we'll add our group.
			con = this.getConnectionPool().getConnection();
			// Call our stored procedure.
			stmt = con.prepareCall("{call UpdateGroup(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			stmt.setInt(1, group.getId());
			stmt.setBoolean(2, group.isActive());
			stmt.setInt(3, group.getCustType());
			stmt.setString(4, group.getCompany_ministry());
			stmt.setString(5, group.getDept_branch());
			stmt.setString(6, group.getAddr1());
			stmt.setString(7, group.getAddr2());
			stmt.setString(8, group.getCity());
			stmt.setString(9, group.getProv());
			stmt.setString(10, group.getCountry());
			stmt.setString(11, group.getPcode());
			stmt.setString(12, group.getPhone());
			stmt.setString(13, group.getFax());
			stmt.setString(14, group.getEmail());
			stmt.setString(15, group.getContact_name());
			stmt.setString(16, group.getContact_phone());
			stmt.setString(17, group.getContact_email());
			stmt.setTimestamp(18, new Timestamp(group.getStart_dt().getTime()));
			stmt.setTimestamp(19, new Timestamp(group.getExpiry_dt().getTime()));
			stmt.setTimestamp(20, new Timestamp(group.getModify_dt().getTime()));
			stmt.setInt(21, group.getModify_user().getId());
			stmt.setString(22, group.getCust_note());
			stmt.setString(23, group.getS_package());
			stmt.setBoolean(24, group.isAuto_expire());
			stmt.setInt(25, group.getDaysleft());
			stmt.setString(26, group.getOrganisation_type());
			stmt.setString(27, group.getContact_fax());
			stmt.setString(28, group.getSap_order());
			stmt.setString(29, group.getSap_customer());
			stmt.execute();
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			try { this.getConnectionPool().closeConnection(con); } catch(Exception ignore) {}
			try { stmt.close(); } catch(Exception ignore) {}			
		}
	}
	
	public int addGroup(Group group) throws DAOException {
		Connection con = null;
		CallableStatement stmt = null;
		int id = -1;
		try {
			// Ok, we'll add our group.
			con = this.getConnectionPool().getConnection();
			// Call our stored procedure.
			stmt = con.prepareCall("{? = call AddGroup(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setBoolean(2, group.isActive());
			stmt.setInt(3, group.getCustType());
			stmt.setString(4, group.getCompany_ministry());
			stmt.setString(5, group.getDept_branch());
			stmt.setString(6, group.getAddr1());
			stmt.setString(7, group.getAddr2());
			stmt.setString(8, group.getCity());
			stmt.setString(9, group.getProv());
			stmt.setString(10, group.getCountry());
			stmt.setString(11, group.getPcode());
			stmt.setString(12, group.getPhone());
			stmt.setString(13, group.getFax());
			stmt.setString(14, group.getEmail());
			stmt.setString(15, group.getContact_name());
			stmt.setString(16, group.getContact_phone());
			stmt.setString(17, group.getContact_email());
			stmt.setTimestamp(18, new Timestamp(group.getStart_dt().getTime()));
			stmt.setTimestamp(19, new Timestamp(group.getExpiry_dt().getTime()));
			stmt.setTimestamp(20, new Timestamp(group.getInsert_dt().getTime()));
			stmt.setInt(21, group.getInsert_user().getId());
			stmt.setTimestamp(22, new Timestamp(group.getModify_dt().getTime()));
			stmt.setInt(23, group.getModify_user().getId());
			stmt.setString(24, group.getCust_note());
			stmt.setString(25, group.getS_package());
			stmt.setBoolean(26, group.isAuto_expire());
			stmt.setInt(27, group.getDaysleft());
			stmt.setString(28, group.getOrganisation_type());
			stmt.setString(29, group.getContact_fax());
			stmt.setString(30, group.getSap_order());
			stmt.setString(31, group.getSap_customer());
			
			stmt.execute();
			
			id = stmt.getInt(1);
			
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			try { this.getConnectionPool().closeConnection(con); } catch(Exception ignore) {}
			try { stmt.close(); } catch(Exception ignore) {}
		}
		return id;
	}
	
	public void deleteGroup(int groupId) throws DAOException {
		Connection con = null;
		CallableStatement stmt = null;

		try {
			// Next we'll lookup the group infomation.
			con = this.getConnectionPool().getConnection();
			// Call our stored procedure.
			stmt = con.prepareCall("{call DeleteGroupById(" + Integer.toString(groupId) + ")}");
			stmt.execute();
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			try { this.getConnectionPool().closeConnection(con); } catch(Exception ignore) {}
			try { stmt.close(); } catch(Exception ignore) {}
		}
	}
	
	/**
	 * Looks up a group that a user belongs to.
	 * @param userid unique identifier for a user.
	 * @return	The group this user belongs to.
	 * @throws DAOException Error occurred when accessing our data source.
	 * @throws ObjectNotFoundException No group was found for this user.
	 */
	public Group lookupGroupByUserId(int userid) throws DAOException, ObjectNotFoundException {
		Group group = null;
		Connection con = null;
		CallableStatement stmt = null;
		ResultSet rs = null;

		try {
			// Next we'll lookup the group infomation.
			con = this.getConnectionPool().getConnection();
			// Call our stored procedure.
			stmt = con.prepareCall("{call LookupGroupByUserId(" + Integer.toString(userid) + ")}");
			// Retrieve result set.
			rs = stmt.executeQuery();
			if(rs.next()) {
				group = this.populateGroup(rs);
			} else {
				throw new ObjectNotFoundException("No Group with the userid " + Integer.toString(userid) + " was found");
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} catch (UnknownCredentialException e) {
			throw new DAOException(e);
		} finally {
			try { this.getConnectionPool().closeConnection(con); } catch(Exception ignore) {}
			try { stmt.close(); } catch(Exception ignore) {}
			try { rs.close(); } catch(Exception ignore) {}
		}
		return group;
	}
	
	/**
	 * Checks to see if a group has access to a product.
	 * @param productid	Unique identifier for the product.
	 * @param groupid	Unique identifier for the group.
	 * @return			Whether the group has access to the product or not.
	 * @throws DAOException Problem communicating with our data source.
	 */
	public boolean isProductAssociatedToGroup(int productid, int groupid) throws DAOException {
		boolean exists = false;
		Connection con = null;
		CallableStatement stmt = null;
		try {
			con = this.getConnectionPool().getConnection();
			stmt = con.prepareCall("{call GroupProductExists(?,?,?)}");
			stmt.setInt(1, productid);
			stmt.setInt(2, groupid);
			stmt.registerOutParameter(3, java.sql.Types.BOOLEAN);
			stmt.execute();
			exists = stmt.getBoolean(3);
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			try { this.getConnectionPool().closeConnection(con); } catch(Exception ignore) {}
			try { stmt.close(); } catch(Exception ignore) {}	
		}
		
		return exists;
	}
	/**
	 * Helper method for populating a group object.
	 * @param rs The result set that contains all the information on our group. The next() method must be called 
	 * 				before passing it into this method.
	 * @return	A populated group object.
	 * @throws SQLException Error occurred when accessing our sql database.
	 * @throws DAOException Error occurred when accessing our data source.
	 * @throws UnknownCredentialException The user's do not have their credentials set.
	 */
	private Group populateGroup(ResultSet rs) throws SQLException, DAOException, UnknownCredentialException {
		
		int id = rs.getInt("id");
		List<UserAccess> ua = new ArrayList<UserAccess>();
		List<GroupProduct> gp = new ArrayList<GroupProduct>();
		DAOAccess dao = new DAOAccess();
		DAOUser daoUser = new DAOUser();
		DAOGroupProduct daoGP = new DAOGroupProduct();
		// We need to lookup Users for modified and inserted users.
		
		// First we'll lookup the users that have access to this group.
		List<User> userids = daoUser.lookupUsersByGroup(id);
		for(int i = 0; i < userids.size(); i++) {
			ua.add(dao.lookupUserAccess(userids.get(i).getId()));
		}
		
		// We need useraccess for our group object, create a list.
		User insertUser = new User();
		User modifyUser =  new User();
		try {
			insertUser = daoUser.lookupUserById(Integer.parseInt(rs.getString("insert_id")));
			modifyUser = daoUser.lookupUserById(Integer.parseInt(rs.getString("modify_id")));
		} catch(ObjectNotFoundException e) {
			// Ignore, leave objects as empty.
		}
		
		gp = daoGP.lookupGroupProductByGroupId(id);
		
		return new Group(id, rs.getBoolean("ACTIVE"), rs.getInt("CUSTTYPE"),
				rs.getString("COMPANY_MINISTRY"), rs.getString("DEPT_BRANCH"), 
				rs.getString("ADDR1"), rs.getString("ADDR2"), rs.getString("city"), 
				rs.getString("prov"), rs.getString("country"), rs.getString("pcode"),
				rs.getString("phone"), rs.getString("fax"), rs.getString("email"), 
				rs.getString("contact_name"), rs.getString("contact_phone"),
				rs.getString("contact_email"), rs.getTimestamp("start_dt"),
				rs.getTimestamp("expiry_dt"), rs.getTimestamp("insert_dt"),
				insertUser, rs.getTimestamp("modify_dt"), modifyUser, rs.getString("cust_note"),
				rs.getString("package"), rs.getBoolean("auto_expire"), 
				rs.getInt("daysleft"), rs.getString("organisation_type"),
				rs.getString("contact_fax"), rs.getString("sap_order"),
				rs.getString("sap_customer"), ua, gp);
	}
}
