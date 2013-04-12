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

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import gov.ca.bc.qp.QPDefender.utility.ObjectUtil;
import gov.ca.bc.qp.qpcommon.authenticate.User;
import gov.ca.bc.qp.qpcommon.code.QPBean;

/**
 * Bean for holding information on groups who have users that access secure resources.
 * @author spencer.tickner
 *
 */
@XmlRootElement(name="group")
@XmlType(name="", propOrder={"id", "active", "custType", "company_ministry",
		"dept_branch", "addr1", "addr2", "city", "prov", "country", "pcode",
		"phone", "fax", "email", "contact_name", "contact_phone", "contact_email",
		"start_dt", "expiry_dt", "insert_dt", "insert_user", "modify_dt", "modify_user", "cust_note",
		"s_package", "auto_expire", "daysleft", "organisation_type", "contact_fax",
		"sap_order", "sap_customer", "useraccess", "groupProducts"})
public class Group implements QPBean {

	// private member variables.
	private int id = -1;
	private boolean active = false;
	private int custType = -1;
	private String company_ministry = "";
	private String dept_branch = "";
	private String addr1 = "";
	private String addr2 = "";
	private String city = "";
	private String prov = "";
	private String country = "";
	private String pcode = "";
	private String phone = "";
	private String fax = "";
	private String email = "";
	private String contact_name = "";
	private String contact_phone = "";
	private String contact_email = "";
	private Date start_dt = null;
	private Date expiry_dt = null;
	private Date insert_dt = null;
	private User insert_user = new User();
	private Date modify_dt = null;
	private User modify_user = new User();
	private String cust_note = "";
	private String s_package = "";
	private boolean auto_expire = false;
	private int daysleft = -1;
	private String organisation_type = "";
	private String contact_fax = "";
	private String sap_order = "";
	private String sap_customer = "";
	private List<UserAccess> useraccess = new ArrayList<UserAccess>() {
		{
			add(new UserAccess());
		}
	};
	private List<GroupProduct> groupProducts = new ArrayList<GroupProduct>() {
		{
			add(new GroupProduct());
		}
	};
	
	
	/**
	 * Constructor for creating an empty object.
	 */
	public Group() {}
	
	/**
	 * Constructor for creating a complete group object.
	 * @param id Unique identifer for this group.
	 * @param active Whether or not this group is active.
	 * @param custType Type of customer this group is.
	 * @param company_ministry The company or ministry this group belongs to.
	 * @param dept_branch The department or branch thid group belongs to.
	 * @param addr1 First physical address line for this group.
	 * @param addr2 Second physical address line for this group.
	 * @param city The main city where this group resides.
	 * @param prov The main province where this group resides.
	 * @param country The main country where this group resides.
	 * @param pcode The postal code for this group.
	 * @param phone The phone number for this group.
	 * @param fax The fax number for this group.
	 * @param email The email address for this group.
	 * @param contact_name the contact name for this group.
	 * @param contact_phone The contacts phone number for this group.
	 * @param contact_email The contacts email for this group.
	 * @param start_dt The date this group started access on.
	 * @param expiry_dt The date this group will expire.
	 * @param insert_dt The date this group was created.
	 * @param insert_id The unique identifier for the user that created this group.
	 * @param modify_dt The date of the last modification to this group.
	 * @param modify_id The unique identifier of the user that modified it.
	 * @param cust_note A note on this group.
	 * @param s_package The package this group belongs to.
	 * @param auto_expire If this group should auto-expire or not.
	 * @param daysleft Teh number of days left before this group expires.
	 * @param organisation_type The type of organisation this group belongs to.
	 * @param contact_fax the contacts fax number.
	 * @param sap_order The sap order number that this group used to pay with.
	 * @param sap_customer Direct mapping to the customer names within SAP.
	 */
	public Group(int id, boolean active, int custType, String company_ministry,
			String dept_branch, String addr1, String addr2, String city,
			String prov, String country, String pcode, String phone,
			String fax, String email, String contact_name,
			String contact_phone, String contact_email, Date start_dt,
			Date expiry_dt, Date insert_dt, User insert_user, Date modify_dt,
			User modify_user, String cust_note, String s_package,
			boolean auto_expire, int daysleft, String organisation_type,
			String contact_fax, String sap_order, String sap_customer,
			List<UserAccess> useraccess, List<GroupProduct> groupProducts) {
		super();
		this.setId(id);
		this.setActive(active);
		this.setCustType(custType);
		this.setCompany_ministry(company_ministry);
		this.setDept_branch(dept_branch);
		this.setAddr1(addr1);
		this.setAddr2(addr2);
		this.setCity(city);
		this.setProv(prov);
		this.setCountry(country);
		this.setPcode(pcode);
		this.setPhone(phone);
		this.setFax(fax);
		this.setEmail(email);
		this.setContact_name(contact_name);
		this.setContact_phone(contact_phone);
		this.setContact_email(contact_email);
		this.setStart_dt(start_dt);
		this.setExpiry_dt(expiry_dt);
		this.setInsert_dt(insert_dt);
		this.setInsert_user(insert_user);
		this.setModify_dt(modify_dt);
		this.setModify_user(modify_user);
		this.setCust_note(cust_note);
		this.setS_package(s_package);
		this.setAuto_expire(auto_expire);
		this.setDaysleft(daysleft);
		this.setOrganisation_type(organisation_type);
		this.setContact_fax(contact_fax);
		this.setSap_order(sap_order);
		this.setSap_customer(sap_customer);
		this.setUseraccess(useraccess);
		this.setGroupProducts(groupProducts);
	}

	@Override
	public boolean isEmpty() {
		Group group = new Group();
		return this.isEqual(group);
	}

	@Override
	public boolean isEqual(QPBean object) {
		boolean equal = false;
		if(object instanceof Group) {
			Group g = (Group)object;
			if(object != null) {
				equal = (
						ObjectUtil.equal(this.getId(), g.getId()) &&
						ObjectUtil.equal(this.isActive(), g.isActive()) &&
						ObjectUtil.equal(this.getCustType(), g.getCustType()) &&
						ObjectUtil.equal(this.getCompany_ministry(), g.getCompany_ministry()) &&
						ObjectUtil.equal(this.getDept_branch(), g.getDept_branch()) &&
						ObjectUtil.equal(this.getAddr1(), g.getAddr1()) &&
						ObjectUtil.equal(this.getAddr2(), g.getAddr2()) &&
						ObjectUtil.equal(this.getCity(), g.getCity()) &&
						ObjectUtil.equal(this.getProv(), g.getProv()) &&
						ObjectUtil.equal(this.getCountry(), g.getCountry()) &&
						ObjectUtil.equal(this.getPcode(), g.getPcode()) &&
						ObjectUtil.equal(this.getPhone(), g.getPhone()) &&
						ObjectUtil.equal(this.getFax(), g.getFax()) &&
						ObjectUtil.equal(this.getEmail(), g.getEmail()) &&
						ObjectUtil.equal(this.getContact_name(), g.getContact_name()) &&
						ObjectUtil.equal(this.getContact_phone(), g.getContact_phone()) &&
						ObjectUtil.equal(this.getContact_email(), g.getContact_email()) &&
						ObjectUtil.equal(this.getStart_dt(), g.getStart_dt()) &&
						ObjectUtil.equal(this.getExpiry_dt(), g.getExpiry_dt()) &&
						ObjectUtil.equal(this.getInsert_dt(), g.getInsert_dt()) &&
						ObjectUtil.equal(this.getInsert_user(), g.getInsert_user()) &&
						ObjectUtil.equal(this.getModify_dt(), g.getModify_dt()) &&
						ObjectUtil.equal(this.getModify_user(), g.getModify_user()) &&
						ObjectUtil.equal(this.getCust_note(), g.getCust_note()) &&
						ObjectUtil.equal(this.getS_package(), g.getS_package()) &&
						ObjectUtil.equal(this.isAuto_expire(), g.isAuto_expire()) &&
						ObjectUtil.equal(this.getDaysleft(), g.getDaysleft()) &&
						ObjectUtil.equal(this.getOrganisation_type(), g.getOrganisation_type()) &&
						ObjectUtil.equal(this.getContact_fax(), g.getContact_fax()) &&
						ObjectUtil.equal(this.getSap_order(), g.getSap_order()) &&
						ObjectUtil.equal(this.getSap_customer(), g.getSap_customer()) &&
						ObjectUtil.equal(this.getUseraccess(), g.getUseraccess())
					);
			}
		}
		return equal;
	}

	/**
	 * @return Unique identifier for this group.
	 */
	@XmlElement(name="id")
	public int getId() {
		return id;
	}

	/**
	 * @param iD Unique identifier for this group.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Whether or not this customer is active. If inactive none of the users associated
	 * 			with this group can access resources.
	 */
	@XmlElement(name="active")
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active Whether or not this customer is active. If inactive none of the users associated
	 * 			with this group can access resources.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return The type of that this group belongs to. 
	 */
	@XmlElement(name="custType")
	public int getCustType() {
		return custType;
	}

	/**
	 * @param custType The type of that this group belongs to. 
	 */
	public void setCustType(int custType) {
		this.custType = custType;
	}

	/**
	 * @return The ministry of company this group belongs to. 
	 */
	@XmlElement(name="company_ministry")
	public String getCompany_ministry() {
		return company_ministry;
	}

	/**
	 * @param company_ministry The ministry of company this group belongs to. 
	 */
	public void setCompany_ministry(String company_ministry) {
		if(company_ministry == null) company_ministry = "";
		this.company_ministry = company_ministry;
	}

	/**
	 * @return The department or branch that this group belongs to.
	 */
	@XmlElement(name="dept_branch")
	public String getDept_branch() {
		return dept_branch;
	}
	
	/**
	 * @param dept_branch The department or branch that this group belongs to.
	 */
	public void setDept_branch(String dept_branch) {
		if(dept_branch == null) dept_branch = "";
		this.dept_branch = dept_branch;
	}

	/**
	 * @return First line in a physical location address.
	 */
	@XmlElement(name="addr1")
	public String getAddr1() {
		return addr1;
	}

	/**
	 * @param addr1 First line in a physical location address.
	 */
	public void setAddr1(String addr1) {
		if(addr1 == null) addr1 = "";
		this.addr1 = addr1;
	}

	/**
	 * @return Second line in a physical location address.
	 */
	@XmlElement(name="addr2")
	public String getAddr2() {
		return addr2;
	}

	/**
	 * @param addr2 Second line in a physical location address.
	 */
	public void setAddr2(String addr2) {
		if(addr2 == null) addr2 = "";
		this.addr2 = addr2;
	}

	/**
	 * @return Main city in which this group resides.
	 */
	@XmlElement(name="city")
	public String getCity() {
		return city;
	}

	/**
	 * @param city  Main city in which this group resides.
	 */ 
	public void setCity(String city) {
		if(city == null) city = "";
		this.city = city;
	}

	/**
	 * @return  Main province in which this group resides.
	 */
	@XmlElement(name="prov")
	public String getProv() {
		return prov;
	}

	/**
	 * @param prov  Main province in which this group resides.
	 */
	public void setProv(String prov) {
		if(prov == null) prov = "";
		this.prov = prov;
	}

	/**
	 * @return  Main Country in which this group resides.
	 */
	@XmlElement(name="country")
	public String getCountry() {
		return country;
	}

	/**
	 * @param country  Main Country in which this group resides.
	 */
	public void setCountry(String country) {
		if(country == null) country = "";
		this.country = country;
	}

	/**
	 * @return Postal code identifier for the given address.
	 */
	@XmlElement(name="pcode")
	public String getPcode() {
		return pcode;
	}

	/**
	 * @param pcode Postal code identifier for the given address.
	 */
	public void setPcode(String pcode) {
		if(pcode == null) pcode = "";
		this.pcode = pcode;
	}

	/**
	 * @return Main contact number for this group via telephone.
	 */
	@XmlElement(name="phone")
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone Main contact number for this group via telephone.
	 */
	public void setPhone(String phone) {
		if(phone == null) phone = "";
		this.phone = phone;
	}

	/**
	 * @return Main fax number for this group.
	 */
	@XmlElement(name="fax")
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax  Main fax number for this group.
	 */
	public void setFax(String fax) {
		if(fax == null) fax = "";
		this.fax = fax;
	}

	/**
	 * @return Main contacts email for this group.
	 */
	@XmlElement(name="email")
	public String getEmail() {
		return email;
	}

	/**
	 * @param email Main contacts email for this group.
	 */
	public void setEmail(String email) {
		if(email == null) email = "";
		this.email = email;
	}

	/**
	 * @return The name of the main contact for this group.
	 */
	@XmlElement(name="contact_name")
	public String getContact_name() {
		return contact_name;
	}

	/**
	 * @param contact_name The name of the main contact for this group.
	 */
	public void setContact_name(String contact_name) {
		if(contact_name == null) contact_name = "";
		this.contact_name = contact_name;
	}

	/**
	 * @return The main contact physical telephone number.
	 */
	@XmlElement(name="contact_phone")
	public String getContact_phone() {
		return contact_phone;
	}

	/**
	 * @param contact_phone The main contact physical telephone number.
	 */
	public void setContact_phone(String contact_phone) {
		if(contact_phone == null) contact_phone = "";
		this.contact_phone = contact_phone;
	}

	/**
	 * @return The main contact electronic mailing address.
	 */
	@XmlElement(name="contact_email")
	public String getContact_email() {
		return contact_email;
	}

	/**
	 * @param contact_email The main contact electronic mailing address.
	 */
	public void setContact_email(String contact_email) {
		if(contact_email == null) contact_email = "";
		this.contact_email = contact_email;
	}

	/**
	 * @return The start date this group is valid from.
	 */
	@XmlElement(name="start_dt")
	public Date getStart_dt() {
		return start_dt;
	}

	/**
	 * @param start_dt The start date this group is valid from.
	 */
	public void setStart_dt(Date start_dt) {
		this.start_dt = start_dt;
	}

	/**
	 * @return Date that this group will expire.
	 */
	@XmlElement(name="expiry_dt")
	public Date getExpiry_dt() {
		return expiry_dt;
	}

	/**
	 * @param expiry_dt Date that this group will expire.
	 */
	public void setExpiry_dt(Date expiry_dt) {
		this.expiry_dt = expiry_dt;
	}

	/**
	 * @return The date this group was inserted into the database.
	 */
	@XmlElement(name="insert_dt")
	public Date getInsert_dt() {
		return insert_dt;
	}

	/**
	 * @param insert_dt The date this group was inserted into the database.
	 */
	public void setInsert_dt(Date insert_dt) {
		this.insert_dt = insert_dt;
	}

	/**
	 * @return Unique identifer of the user that inserted this group.
	 */
	@XmlElement(name="insert_user")
	public User getInsert_user() {
		return insert_user;
	}

	/**
	 * @param insert_id Unique identifier of the user that inserted this group.
	 */
	public void setInsert_user(User insert_user) {
		this.insert_user = insert_user;
	}

	/**
	 * @return The last time this group was modified.
	 */
	@XmlElement(name="modify_dt")
	public Date getModify_dt() {
		return modify_dt;
	}

	/**
	 * @param modify_dt The last time this group was modified.
	 */
	public void setModify_dt(Date modify_dt) {
		this.modify_dt = modify_dt;
	}

	/**
	 * @return The user of the last user that modified this group.
	 */
	@XmlElement(name="modify_user")
	public User getModify_user() {
		return modify_user;
	}

	/**
	 * @param modify_id The last user that modified this group.
	 */
	public void setModify_user(User modify_user) {
		this.modify_user = modify_user;
	}

	/**
	 * @return A note for this group.
	 */
	@XmlElement(name="cust_note")
	public String getCust_note() {
		return cust_note;
	}

	/**
	 * @param cust_note A note for this group.
	 */
	public void setCust_note(String cust_note) {
		if(cust_note == null) cust_note = "";
		this.cust_note = cust_note;
	}

	/**
	 * @return package that this group belongs to.
	 */
	@XmlElement(name="s_package")
	public String getS_package() {
		return s_package;
	}

	/**
	 * @param s_package package that this group belongs to.
	 */
	public void setS_package(String s_package) {
		if(s_package == null) s_package = "";
		this.s_package = s_package;
	}

	/**
	 * @return Whether or not this group should auto expire.
	 */
	@XmlElement(name="auto_expire")
	public boolean isAuto_expire() {
		return auto_expire;
	}

	/**
	 * @param auto_expire Whether or not this group should auto expire.
	 */
	public void setAuto_expire(boolean auto_expire) {
		this.auto_expire = auto_expire;
	}

	/**
	 * @return Number of days left before this group expires.
	 */
	@XmlElement(name="daysleft")
	public int getDaysleft() {
		return daysleft;
	}

	/**
	 * @param daysleft Number of days left before this group expires.
	 */
	public void setDaysleft(int daysleft) {
		this.daysleft = daysleft;
	}

	/**
	 * @return The organisation type this group belongs to.
	 */
	@XmlElement(name="organisation_type")
	public String getOrganisation_type() {
		return organisation_type;
	}

	/**
	 * @param organisation_type The organisation type this group belongs to.
	 */
	public void setOrganisation_type(String organisation_type) {
		if(organisation_type == null) organisation_type = "";
		this.organisation_type = organisation_type;
	}

	/**
	 * @return the contact for this groups fax number.
	 */
	@XmlElement(name="contact_fax")
	public String getContact_fax() {
		return contact_fax;
	}

	/**
	 * @param contact_fax the contact for this groups fax number.
	 */
	public void setContact_fax(String contact_fax) {
		if(contact_fax == null) contact_fax = "";
		this.contact_fax = contact_fax;
	}

	/**
	 * @return The sap order number that this group used to pay with.
	 */
	@XmlElement(name="sap_order")
	public String getSap_order() {
		return sap_order;
	}

	/**
	 * @param sap_order The sap order number that this group used to pay with.
	 */
	public void setSap_order(String sap_order) {
		if(sap_order == null) sap_order = "";
		this.sap_order = sap_order;
	}

	/**
	 * @return The customer this group is associated with within SAP.
	 */
	@XmlElement(name="sap_customer")
	public String getSap_customer() {
		return sap_customer;
	}

	/**
	 * @param sap_customer The customer this group is associated with within SAP.
	 */
	public void setSap_customer(String sap_customer) {
		if(sap_customer == null) sap_customer = "";
		this.sap_customer = sap_customer;
	}
	

	/**
	 * @return The users for this group and their access levels.
	 */
	@XmlElementWrapper(name="users")
	@XmlElement(name="useraccess")
	public List<UserAccess> getUseraccess() {
		return useraccess;
	}

	/**
	 * @param useraccess The users for this group and their access levels.
	 */
	public void setUseraccess(List<UserAccess> useraccess) {
		this.useraccess = useraccess;
	}

	/**
	 * @return the products that this group has access to.
	 */
	@XmlElementWrapper(name="groupProducts")
	@XmlElement(name="groupProduct")
	public List<GroupProduct> getGroupProducts() {
		return groupProducts;
	}

	/**
	 * @param groupProducts the products that this group has access to.
	 */
	public void setGroupProducts(List<GroupProduct> groupProducts) {
		this.groupProducts = groupProducts;
	}
	
	
	
}
