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
import gov.ca.bc.qp.qpcommon.code.ObjectNotFoundException;
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

	/**
	 * Looks up a product associated with a group by it's unique identifier
	 * @param gpid	The unique identifier for this specific group/product information.
	 * @return	The product and specific group information for the unique identifier.
	 * @throws DAOException	An error occurred when accessing our data source.
	 * @throws ObjectNotFoundException No group product with this unique id exists.
	 */
	public GroupProduct lookupGroupProductById(int gpid) throws DAOException, ObjectNotFoundException {
		Connection con = null;
		CallableStatement stmt = null;
		ResultSet rs = null;
		DAOProducts dao = new DAOProducts();
		GroupProduct gp = new GroupProduct();
		try {
			con = this.getConnectionPool().getConnection();
			// Call our stored procedure.
			stmt = con.prepareCall("{call lookupGroupProductById(" + Integer.toString(gpid) + ")}");
			// Retrieve result set.
			rs = stmt.executeQuery();
			if(rs.next()) {
				Product p = dao.lookupProductById(rs.getInt("productId"));
				gp = new GroupProduct(rs.getInt("ID"), 
					rs.getInt("GroupId"), p, rs.getInt("Concurrent"),
					rs.getDate("ExpiryDate"));
			} else {
				throw new ObjectNotFoundException("No group product with that id exists");
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			// Close all our resources. Ignore any exceptions to give each a chance to close.
			try { this.getConnectionPool().closeConnection(con); } catch(Exception ignore) {}
			try { rs.close(); } catch(Exception ignore) {}
			try { stmt.close(); } catch (Exception ignore) {}
			
		}
		return gp;	
	}
	
	/**
	 * Deletes a groups access to a product. Does not delete the group or product.
	 * @param id Unique identifier of this groups access to the product.
	 * @throws DAOException Problem occurred while accessing our data source.
	 */
	public void deleteGroupProduct(int id) throws DAOException {
		Connection con = null;
		CallableStatement stmt = null;
		
		try {
			con = this.getConnectionPool().getConnection();
			stmt = con.prepareCall("{call DeleteGroupProduct(?)}");
			stmt.setInt(1, id);
			stmt.execute();
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			// Close all our resources. Ignore any exceptions to give each a chance to close.
			try { this.getConnectionPool().closeConnection(con); } catch(Exception ignore) {}
			try { stmt.close(); } catch (Exception ignore) {}			
		}
		
	}
}
