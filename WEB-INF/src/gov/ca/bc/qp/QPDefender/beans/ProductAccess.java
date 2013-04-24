package gov.ca.bc.qp.QPDefender.beans;

import java.util.List;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import gov.ca.bc.qp.qpcommon.authenticate.Product;
import gov.ca.bc.qp.qpcommon.authenticate.Role;
import gov.ca.bc.qp.qpcommon.code.QPBean;

@XmlRootElement(name="productAccess")
@XmlType(name="", propOrder={"userProductsID", "userid", "timeout", "active", "product", "roles"})
public class ProductAccess implements QPBean {

	// private member variables.
	private int userProductsID = -1;
	private int userid = -1;
	private Product product = new Product();
	private List<Role> roles = new ArrayList<Role>() {
		{
			add(new Role());
		}
	};
	private int timeout = -1;
	private boolean active = false;
	
	/**
	 * Constructor for creating empty ProductAccess
	 */
	public ProductAccess() {}
	
	/**
	 * Creating full instance of a product access object.
	 * @Param userProductsID unique identifier for this user's product and role constraint.
	 * @param userid Unique identifier for the a user.
	 * @param product A product that the user has access to.
	 * @param roles The roles the user belongs to for this product.
	 * @param timeout The amount of minutes of innactivity before the user is automatically logged out.
	 * @param active Whether the user is in an active state for this product.
	 */
	public ProductAccess(int userProductsID, int userid, Product product, List<Role> roles, int timeout, boolean active) {
		this.setUserProductsID(userProductsID);
		this.setUserid(userid);
		this.setProduct(product);
		this.setRoles(roles);
		this.setTimeout(timeout);
		this.setActive(active);
	}
	
	/**
	 * @return unique identifier for this user's product and role constraint.
	 */
	@XmlElement(name="userProductsID")
	public int getUserProductsID() {
		return this.userProductsID;
	}
	
	/**
	 * @param userProductsID unique identifier for this user's product and role constraint.
	 */
	public void setUserProductsID(int userProductsID) {
		this.userProductsID = userProductsID;
	}
	/**
	 * @return Unique identifier for a user.
	 */
	@XmlElement(name="userid")
	public int getUserid() {
		return userid;
	}
	/**
	 * @param userid Unique identifier for a user.
	 */
	public void setUserid(int userid) {
		this.userid = userid;
	}
	/**
	 * @return A product that this userid has access to.
	 */
	@XmlElement(name="product")
	public Product getProduct() {
		return product;
	}
	/**
	 * @param product A product that this userid has access to.
	 */
	public void setProduct(Product product) {
		this.product = product;
	}
	/**
	 * @return The roles that this user has for this product.
	 */
	@XmlElementWrapper(name="roles")
	@XmlElement(name="role")
	public List<Role> getRoles() {
		return roles;
	}
	/**
	 * @param roles The roles 0that this user has for this product.
	 */
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	/**
	 * @return The time in minutes before this user is denied access to this product.
	 */
	@XmlElement(name="timeout")
	public int getTimeout() {
		return timeout;
	}
	/**
	 * @param timeout The time in minutes before this user is denied access to this product.
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	/**
	 * @return Whether this userid access to this product is in an active state.
	 */
	@XmlElement(name="active")
	public boolean isActive() {
		return active;
	}
	/**
	 * @param active Whether this userid access to this product is in an active state.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	
	
	@Override
	public boolean isEmpty() {
		ProductAccess pa = new ProductAccess();
		return this.isEqual(pa);
	}
	@Override
	public boolean isEqual(QPBean object) {
		boolean equal = false;
		if(this.getProduct() == null || this.getRoles() == null || !(object instanceof ProductAccess)) {
			equal = false;
		} else {
			ProductAccess pa = (ProductAccess)object;
			if(this.getProduct().isEqual(pa.getProduct()) && this.getTimeout() == pa.getTimeout() &&
					this.getUserid() == pa.getUserid()) {
				if(this.getRoles().size() == pa.getRoles().size()) {
					equal = true;
					for(int i = 0; i < this.getRoles().size(); i++) {
						if(!this.getRoles().get(i).isEqual(pa.getRoles().get(i))) {
							equal = false;
							break;
						}
					}
				}
			}
		}
		return equal;
	}
	
	
}
