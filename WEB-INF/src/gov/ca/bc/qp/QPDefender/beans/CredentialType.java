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

import gov.ca.bc.qp.QPDefender.utility.ObjectUtil;
import gov.ca.bc.qp.qpcommon.code.QPBean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Bean for holding the different types of credentials we can authenticate against.
 * @author spencer.tickner
 *
 */
@XmlRootElement(name="credentialType")
@XmlType(name="", propOrder={"id", "type", "description"})
public class CredentialType implements QPBean {

	// Private member variables.
	private int id = -1;
	private String type = "";
	private String Description = "";
	
	/**
	 * Constructor for instantiating a complete CredentialType bean.
	 * @param id Unique identifier for this credential type.
	 * @param type	Human readable name given to this credential type.
	 * @param description A description of what this authentication mechanism entails.
	 */
	public CredentialType(int id, String type, String description) {
		this.setId(id);
		this.setType(type);
		this.setDescription(description);
	}
	
	/**
	 * Empty Constructor.
	 */
	public CredentialType() {}
	
	/**
	 * @return Unique identifier for this credential type.
	 */
	@XmlElement(name="id")
	public int getId() {
		return id;
	}
	/**
	 * @return Human readable name given to this credential type.
	 */
	@XmlElement(name="type")
	public String getType() {
		return type;
	}
	/**
	 * @return A description of what this authentication mechanism entails.
	 */
	@XmlElement(name="description")
	public String getDescription() {
		return Description;
	}
	/**
	 * @param id Unique identifier for this credential type.
	 */
	private void setId(int id) {
		this.id = id;
	}
	/**
	 * @param type Human readable name given to this credential type.
	 */
	private void setType(String type) {
		this.type = type;
	}
	/**
	 * @param description A description of what this authentication mechanism entails.
	 */
	private void setDescription(String description) {
		Description = description;
	}
	
	
	@Override
	public boolean isEmpty() {
		CredentialType ct = new CredentialType();
		return this.isEqual(ct);
	}
	@Override
	public boolean isEqual(QPBean obj) {
		boolean equal = false;
		if(obj instanceof CredentialType) {
			CredentialType ct = (CredentialType)obj;
			if(ObjectUtil.equal(ct.getId(), this.getId()) && ObjectUtil.equal(ct.getType(), this.getType()) &&
					ObjectUtil.equal(ct.getDescription(), this.getDescription())) {
				equal = true;
			}
		}
		return equal;
	}
	
	
	
}
