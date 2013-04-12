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

import gov.ca.bc.qp.qpcommon.authenticate.User;
import gov.ca.bc.qp.qpcommon.code.QPBean;

import java.util.List;
import java.util.ArrayList;

/**
 * A class for compiling access information for Users.
 * @author spencer.tickner
 *
 */
public class UserAccess implements QPBean {

	// Private member variables.
	private User user = new User();
	private List<ProductAccess> productAccess = new ArrayList<ProductAccess>() {
		{
			add(new ProductAccess());
		}
	};
	private String credentialType = "";
	private String credential = "";
	private String credential2 = "";
	
	/**
	 * Constructor for creating empty elements.
	 */
	public UserAccess() {}
	
	/**
	 * Default Constructor for creating a full UserAccess Bean.
	 * @param user The user associated with this access.
	 * @param productAccess	The products that this user has access to.
	 * @param credentialType The way this user accesses the products.
	 * @param credential Can be a password, or IP address.
	 * @param credential2 A subnet mask when applicable.
	 */
	public UserAccess(User user, List<ProductAccess> productAccess, String credentialType,
			String credential, String credential2) {
		this.setUser(user);
		this.setProductAccess(productAccess);
		this.setCredentialType(credentialType);
		this.setCredential(credential);
		this.setCredential2(credential2);
	}
	
	/**
	 * @return The user associated with this access.
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * @param user The user associated with this access.
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return The products that this user has access to.
	 */
	public List<ProductAccess> getProductAccess() {
		return productAccess;
	}

	/**
	 * @param productAccess The products that this user has access to.
	 */
	public void setProductAccess(List<ProductAccess> productAccess) {
		this.productAccess = productAccess;
	}

	/**
	 * @return The way this user accesses the products.
	 */
	public String getCredentialType() {
		return credentialType;
	}

	/**
	 * @param credentialType The way this user accesses the products.
	 */
	public void setCredentialType(String credentialType) {
		this.credentialType = credentialType;
	}

	/**
	 * @return Can be a password, or IP address.
	 */
	public String getCredential() {
		return credential;
	}

	/**
	 * @param credential Can be a password, or IP address.
	 */
	public void setCredential(String credential) {
		this.credential = credential;
	}

	/**
	 * @return A subnet mask when applicable.
	 */
	public String getCredential2() {
		return credential2;
	}

	/**
	 * @param credential2 A subnet mask when applicable.
	 */
	public void setCredential2(String credential2) {
		this.credential2 = credential2;
	}

	@Override
	public boolean isEmpty() {
		UserAccess ua = new UserAccess();
		return this.isEqual(ua);
	}

	@Override
	public boolean isEqual(QPBean object) {
		if(this.getUser() == null || this.getProductAccess() == null || !(object instanceof UserAccess)) {
			return false;
		} else {
			UserAccess u = (UserAccess)object;
			// OK start with our products.
			if(this.getProductAccess().size() != u.getProductAccess().size())
				return false;
			
			for(int i = 0; i < this.getProductAccess().size(); i++) {
				if(!this.getProductAccess().get(i).isEqual(u.getProductAccess().get(i))) {
					return false;
				}
			}

			if(this.getUser().isEqual(u.getUser()) && this.getCredential().equals(u.getCredential()) &&
					this.getCredentialType().equals(u.getCredentialType())) {
				// Credential 2 may be null
				if(this.getCredential2() == null || u.getCredential2() == null) {
					if(this.getCredential2() != u.getCredential2()) {
						return false;
					} else {
						return true;
					}
				} else if(this.getCredential2().equals(u.getCredential2())) {
					return true;
				}
			}
		}
		return false;
	}

	
}
