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

import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import gov.ca.bc.qp.QPDefender.utility.ObjectUtil;
import gov.ca.bc.qp.qpcommon.authenticate.Product;
import gov.ca.bc.qp.qpcommon.code.QPBean;

/**
 * Object for tracking groups and the products they have access to.
 * @author spencer.tickner
 */
@XmlRootElement(name="groupproduct")
@XmlType(name="", propOrder={"id", "groupid", "product", "concurrent", "expiryDate"})
public class GroupProduct implements QPBean {

	private int id = -1;
	private int groupid = -1;
	private Product product = new Product();
	private int concurrent = -1;
	private Date expiryDate = null;
	
	/**
	 * Creates an empty group product object.
	 */
	public GroupProduct() {}
	
	/**
	 * Creates a fully instantiated group product object.
	 * @param id Unique identifier for this group product.
	 * @param groupid Unique identifier for a group.
	 * @param product A product that this group has access to.
	 * @param concurrent The number of users that can access this product concurrently.
	 * @param expiryDate The date that this product with expire for this group.
	 */
	public GroupProduct(int id, int groupid, Product product, int concurrent,
			Date expiryDate) {
		super();
		this.setId(id);
		this.setGroupid(groupid);
		this.setProduct(product);
		this.setConcurrent(concurrent);
		this.setExpiryDate(expiryDate);
	}
	
	/**
	 * @return Unique identifier for this group product.
	 */
	@XmlElement(name="id")
	public int getId() {
		return id;
	}
	/**
	 * @return Unique identifier for a group.
	 */
	@XmlElement(name="groupid")
	public int getGroupid() {
		return groupid;
	}
	/**
	 * @return A product that this group has access to.
	 */
	@XmlElement(name="product")
	public Product getProduct() {
		return product;
	}
	/**
	 * @return The number of users that can access this product concurrently.
	 */
	@XmlElement(name="concurrent")
	public int getConcurrent() {
		return concurrent;
	}
	/**
	 * @return The date that this product with expire for this group.
	 */
	@XmlElement(name="expiryDate")
	public Date getExpiryDate() {
		return expiryDate;
	}
	
	/**
	 * @param id Unique identifier for this group product.
	 */
	private void setId(int id) {
		this.id = id;
	}
	/**
	 * @param groupid Unique identifier for a group.
	 */
	private void setGroupid(int groupid) {
		this.groupid = groupid;
	}
	/**
	 * @param product A product that this group has access to.
	 */
	private void setProduct(Product product) {
		this.product = product;
	}
	/**
	 * @param concurrent The number of users that can access this product concurrently.
	 */
	private void setConcurrent(int concurrent) {
		this.concurrent = concurrent;
	}
	/**
	 * @param expiryDate The date that this product with expire for this group.
	 */
	private void setExpiryDate(Date expiryDate) {
		// Remove milliseconds from dates as they cause comparison problems between DB and JAVA
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(expiryDate);
		calendar.set(Calendar.MILLISECOND, 0);
		this.expiryDate = expiryDate;
	}

	@Override
	public boolean isEmpty() {
		GroupProduct gp = new GroupProduct();
		return isEqual(gp);
	}

	@Override
	public boolean isEqual(QPBean object) {
		boolean equal = false;
		if(object == null || !(object instanceof GroupProduct)) {
			equal = false;
		} else {
			GroupProduct g = (GroupProduct)object;
			equal = (ObjectUtil.equal(this.getId(), g.getId()) &&
					ObjectUtil.equal(this.getConcurrent(), g.getConcurrent()) &&
					ObjectUtil.equal(this.getExpiryDate(), g.getExpiryDate()) &&
					ObjectUtil.equal(this.getGroupid(), g.getGroupid()) &&
					ObjectUtil.equal(this.getProduct(), g.getProduct())
				);
		}
		return equal;
	}
	
	
	
	
}
