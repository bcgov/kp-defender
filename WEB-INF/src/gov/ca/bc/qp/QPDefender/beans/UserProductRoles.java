/*
 * Copyright (c) 2013, Queen's Printer of British Columbia, Canada and/or its affiliates. 
 * All rights reserved. DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE Header.
 * 
 * Please contact Queen's Printer of British Columbia, PO Box 9452 Stn Prov Govt, Victoria 
 * BC, V8W 9V7, (250) 387-3309 if you have any questions or have received this class in 
 * error.
 * 
 */
package gov.ca.bc.qp.QPDefender.beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import gov.ca.bc.qp.QPDefender.utility.ObjectUtil;
import gov.ca.bc.qp.qpcommon.authenticate.Role;
import gov.ca.bc.qp.qpcommon.code.QPBean;

@XmlRootElement(name="UserProductRoles")
@XmlType(name="", propOrder={"userProductRolesId","userProductId", "productRolesId", "role"})
public class UserProductRoles implements QPBean {

	// Private member variables.
	private int userProductRolesId = -1;
	private int userProductId = -1;
	private int productRolesId = -1;
	private Role role = new Role();
	
	/**
	 * Empty constructor for creating a UserProductRoles.
	 */
	public UserProductRoles() {}
	
	/**
	 * Instantiates a non-empty UserProductRoles object.
	 * @param userProductRolesId The unique identifier for this set of roles and credentials.
	 * @param userProductId	The identifier of a product that a user has access to.
	 * @param productRolesId The identifier for a products roles that this user is a member of.
	 * @param role The role that this user has access to for this product.
	 */
	public UserProductRoles(int userProductRolesId, int userProductId,
			int productRolesId, Role role) {
		super();
		this.setUserProductRolesId(userProductRolesId);
		this.setUserProductId(userProductId);
		this.setProductRolesId(productRolesId);
		this.setRole(role);
	}

	/**
	 * @return The unique identifier for this set of roles and credentials.
	 */
	@XmlElement(name="userProductRolesId")
	public int getUserProductRolesId() {
		return userProductRolesId;
	}
	/**
	 * @return 	The identifier of a product that a user has access to.
	 */
	@XmlElement(name="userProductId")
	public int getUserProductId() {
		return userProductId;
	}
	/**
	 * @return The identifier for a products roles that this user is a member of.
	 */
	@XmlElement(name="productRolesId")
	public int getProductRolesId() {
		return productRolesId;
	}
	/**
	 * @return The role that this user has access to for this product.
	 */
	@XmlElement(name="role")
	public Role getRole() {
		return role;
	}

	/**
	 * @param userProductRolesId The unique identifier for this set of roles and credentials.
	 */
	private void setUserProductRolesId(int userProductRolesId) {
		this.userProductRolesId = userProductRolesId;
	}

	/**
	 * @param userProductId The identifier of a product that a user has access to.
	 */
	private void setUserProductId(int userProductId) {
		this.userProductId = userProductId;
	}

	/**
	 * @param productRolesId The identifier for a products roles that this user is a member of.
	 */
	private void setProductRolesId(int productRolesId) {
		this.productRolesId = productRolesId;
	}

	/**
	 * @param role The role that this user has access to for this product.
	 */
	private void setRole(Role role) {
		this.role = role;
	}

	@Override
	public boolean isEmpty() {
		UserProductRoles upr = new UserProductRoles();
		return isEqual(upr);
	}

	@Override
	public boolean isEqual(QPBean object) {
		
		boolean equal = false;
		if(object == null || !(object instanceof UserProductRoles)) {
			equal = false;
		} else {
			UserProductRoles upr = (UserProductRoles)object;
			if(ObjectUtil.equal(this.getUserProductRolesId(), upr.getUserProductRolesId()) &&
					ObjectUtil.equal(this.getUserProductId(), upr.getUserProductId()) &&
					ObjectUtil.equal(this.getProductRolesId(), upr.getProductRolesId()) &&
					ObjectUtil.equal(this.getRole(), upr.getRole())) {
				equal = true;
			}
		}
		return equal;
	}
	
	
	
}
