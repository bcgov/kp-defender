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

import gov.ca.bc.qp.QPDefender.beans.GroupProduct;
import gov.ca.bc.qp.qpcommon.authenticate.DAOProducts;
import gov.ca.bc.qp.qpcommon.authenticate.Product;
import gov.ca.bc.qp.qpcommon.connection.DAOException;
import gov.ca.bc.qp.qpcommon.connection.DAOSecurity;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for accessing Groups and their associated products from our datasource.
 * @author spencer.tickner
 */
public class DAOGroupProduct extends DAOSecurity  {

	/**
	 * Looks up a group, and it's associated product information from our data source.
	 * @param groupid	Unique identifier for the group we want to look up.
	 * @return a list of products and their associated access information for a particular group.
	 * @throws DAOException An error occurred when accessing our data source.
	 */
	public List<GroupProduct> lookupGroupProductByGroupId(int groupid) throws DAOException {
		List<GroupProduct> gps = new ArrayList<GroupProduct>();
		Connection con = null;
		CallableStatement stmt = null;
		ResultSet rs = null;
		DAOProducts dao = new DAOProducts();
		try {
			con = this.getConnectionPool().getConnection();
			// Call our stored procedure.
			stmt = con.prepareCall("{call lookupGroupProductByGroupId(" + Integer.toString(groupid) + ")}");
			// Retrieve result set.
			rs = stmt.executeQuery();
			while(rs.next()) {
				Product p = dao.lookupProductById(rs.getInt("productId"));
				GroupProduct gp = new GroupProduct(rs.getInt("ID"), 
					rs.getInt("GroupId"), p, rs.getInt("Concurrent"),
					rs.getDate("ExpiryDate"));
				gps.add(gp);
			}			
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			// Close all our resources. Ignore any exceptions to give each a chance to close.
			try { this.getConnectionPool().closeConnection(con); } catch(Exception ignore) {}
			try { rs.close(); } catch(Exception ignore) {}
			try { stmt.close(); } catch (Exception ignore) {}
			
		}
		return gps;
	}
}
