package gov.ca.bc.qp.QPDefender.beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import gov.ca.bc.qp.qpcommon.code.QPBean;

/**
 * The different types of groups that are tracked.
 * @author spencer.tickner
 *
 */
@XmlRootElement(name="CustType")
@XmlType(name="", propOrder={"id", "custType", "description"})
public class CustType implements QPBean {

	// Private member variables.
	private int id = -1;
	private String custType = "";
	private String description = "";
	
	/**
	 * Empty constructor.
	 */
	public CustType() {}
	
	/**
	 * Instantiates a complete CustType object.
	 * @param id Unique identifier for this type.
	 * @param custType	Name of this customer type.
	 * @param description	A description of what this customer type refers to.
	 */
	public CustType(int id, String custType, String description) {
		super();
		this.setId(id);
		this.setCustType(custType);
		this.setDescription(description);
	}
	/**
	 * @return Unique identifier for this type.
	 */
	@XmlElement(name="id")
	public int getId() {
		return id;
	}
	/**
	 * @return Name of this customer type.
	 */
	@XmlElement(name="custType")
	public String getCustType() {
		return custType;
	}
	/**
	 * @return A description of what this customer type refers to.
	 */
	@XmlElement(name="description")
	public String getDescription() {
		return description;
	}
	/**
	 * @param id Unique identifier for this type.
	 */
	private void setId(int id) {
		this.id = id;
	}
	/**
	 * @param custType Name of this customer type.
	 */
	private void setCustType(String custType) {
		this.custType = custType;
	}
	/**
	 * @param description A description of what this customer type refers to.
	 */
	private void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEqual(QPBean object) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	
}
