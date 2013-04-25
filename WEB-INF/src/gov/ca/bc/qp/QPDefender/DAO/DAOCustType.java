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

import gov.ca.bc.qp.QPDefender.beans.CustType;
import gov.ca.bc.qp.qpcommon.connection.DAOException;
import gov.ca.bc.qp.qpcommon.connection.DAOSecurity;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO class for interacting with our data source on customer types.
 * @author spencer.tickner
 */
public class DAOCustType extends DAOSecurity  {

	/**
	 * Looks up all Customer Types.
	 * @return A List of Customer Types
	 * @throws DAOException An error occurred when accessing our datasource.
	 */
	public List<CustType> getAllCustType() throws DAOException {
		List<CustType> types = null;
		Connection con = null;
		CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			con = this.getConnectionPool().getConnection();
			stmt = con.prepareCall("{call LookupCustType()}");
			rs = stmt.executeQuery();
			while(rs.next()) {
				CustType c = new CustType(rs.getInt("ID"), rs.getString("Cust_Type"), rs.getString("Description"));
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			// Cleanup
			try { this.getConnectionPool().closeConnection(con); } catch(Exception ignore) {}
			try { stmt.close(); } catch(Exception ignore) {}
			try { rs.close(); } catch(Exception ignore) {}
		}
		
		
		return types;
	}
}
