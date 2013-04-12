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
import java.util.ArrayList;
import java.util.List;

import gov.ca.bc.qp.QPDefender.beans.Group;
import gov.ca.bc.qp.QPDefender.beans.GroupProduct;
import gov.ca.bc.qp.QPDefender.beans.ProductAccess;
import gov.ca.bc.qp.QPDefender.beans.UnknownCredentialException;
import gov.ca.bc.qp.QPDefender.beans.UserAccess;
import gov.ca.bc.qp.qpcommon.authenticate.DAOUser;
import gov.ca.bc.qp.qpcommon.authenticate.Product;
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
		}		
		return group;
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
				rs.getString("contact_email"), rs.getDate("start_dt"),
				rs.getDate("expiry_dt"), rs.getDate("insert_dt"),
				insertUser, rs.getDate("modify_dt"), modifyUser, rs.getString("cust_note"),
				rs.getString("package"), rs.getBoolean("auto_expire"), 
				rs.getInt("daysleft"), rs.getString("organisation_type"),
				rs.getString("contact_fax"), rs.getString("sap_order"),
				rs.getString("sap_customer"), ua, gp);
	}
}
