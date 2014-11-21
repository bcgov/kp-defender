package gov.ca.bc.qp.QPDefender.web;

import junit.framework.Assert;

import org.junit.Test;

import gov.ca.bc.qp.QPDefender.test.Utility;

public class MessageTest {

	private static final String expectedSuccess = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><message>Success</message></root>";
	@Test
	public void testMessage() {
		String msg = Utility.getStringFromDoc(Message.SUCCESS.getMessage());
		Assert.assertEquals(expectedSuccess, msg);
	}
}
