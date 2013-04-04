package gov.ca.bc.qp.QPDefender.web;

import java.net.URL;

import gov.ca.bc.qp.QPDefender.config.MyResolver;
import gov.ca.bc.qp.qpcommon.dom.QPSchemaValidator;
import gov.ca.bc.qp.qpcommon.dom.ValidationException;

import javax.ws.rs.core.Response;

import junit.framework.Assert;

import org.junit.Test;
import org.w3c.dom.Document;


/**
 * Class for testing web interface for dealing with poducts.
 * @author spencer.tickner
 */
public class WebProductTest extends gov.ca.bc.qp.QPDefender.test.DataSourceTestUtil {
	
	@Test
	public void testGetProducts() {
		WebProduct product = new WebProduct();
		// Suppress transformation
		product.xsl_global = MyResolver.NO_TRANSFORM;
		Response response = product.getProducts();
		Document doc = (Document)response.getEntity();
		URL xsd = WebProductTest.class.getResource("/schema/products.xsd");
		try {
			QPSchemaValidator validator = QPSchemaValidator.getInstance(xsd);
			Assert.assertTrue(validator.validate(doc));
		} catch (ValidationException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testGetProductById() {
		WebProduct product = new WebProduct();
		// Suppress transformation
		product.xsl_global = MyResolver.NO_TRANSFORM;
		Response response = product.getProductById("1");
		Document doc = (Document)response.getEntity();
		URL xsd = WebProductTest.class.getResource("/schema/product.xsd");
		try {
			QPSchemaValidator validator = QPSchemaValidator.getInstance(xsd);
			Assert.assertTrue(validator.validate(doc));
		} catch (ValidationException e) {
			e.printStackTrace();
			Assert.fail();
		}	
	}
	
	@Test
	public void testGetProductByIdWithTransform() {
		WebProduct product = new WebProduct();
		// create transformation
		product.xsl_global = "test";
		Response response = product.getProductById("1");
		Document doc = (Document)response.getEntity();
		Assert.assertEquals("success", doc.getChildNodes().item(0).getLocalName());
			
	}
	

}
