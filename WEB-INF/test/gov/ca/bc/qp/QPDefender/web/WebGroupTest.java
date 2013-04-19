package gov.ca.bc.qp.QPDefender.web;

import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;

import gov.ca.bc.qp.QPDefender.DAO.DAOGroupProduct;
import gov.ca.bc.qp.QPDefender.beans.Group;
import gov.ca.bc.qp.QPDefender.beans.GroupProduct;
import gov.ca.bc.qp.QPDefender.config.MyResolver;
import gov.ca.bc.qp.qpcommon.authenticate.QPPrincipal;
import gov.ca.bc.qp.qpcommon.connection.DAOException;
import gov.ca.bc.qp.qpcommon.dom.QPSchemaValidator;
import gov.ca.bc.qp.qpcommon.dom.ValidationException;
import gov.ca.bc.qp.qpcommon.marshal.QPMarshaller;

import org.junit.Test;
import org.w3c.dom.Document;

public class WebGroupTest extends gov.ca.bc.qp.QPDefender.test.DataSourceTestUtil  {

	private static final int groupid = 1;
	private static final int userid = 1;
	
	@Test
	public void testWebGroup()  {
		WebGroup web = new WebGroup();
		web.xsl_global = MyResolver.NO_TRANSFORM;
		Document doc = (Document)web.getGroup(Integer.toString(groupid)).getEntity();
		try {
			QPSchemaValidator validator = QPSchemaValidator.getInstance(this.getClass().getResource("/schema/group.xsd"));
			Assert.assertTrue(validator.validate(doc));
		} catch (ValidationException e) {
			e.printStackTrace();
			Assert.fail();
		}	
	}
	
	@Test
	public void testGetMyGroup() {
		QPPrincipal principal = new QPPrincipal("stickner", null, userid, 1);
		WebGroup web = new WebGroup();
		web.principal = principal;
		web.xsl_global = MyResolver.NO_TRANSFORM;
		Document doc = (Document)web.getMyGroup().getEntity();
		QPMarshaller marshaller = new QPMarshaller();
		try {
			Group g = (Group) marshaller.unmarshal(doc, new Group());
			Assert.assertNotNull(g);
		} catch (JAXBException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testMarshalGroupProduct() {
		DAOGroupProduct dao = new DAOGroupProduct();
		List<GroupProduct> gps = null;
		try {
			gps = dao.lookupGroupProductByGroupId(1);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		QPMarshaller marshaller = new QPMarshaller();
		Document doc = null;
		try {
			doc = marshaller.marshalToDom(gps.get(0));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(Utility.getStringFromDoc(doc));
	}
}
