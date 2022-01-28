/*
 * Copyright (c) 2013, Queen's Printer of British Columbia, Canada and/or its affiliates. 
 * All rights reserved. DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE Header.
 * 
 * Please contact Queen's Printer of British Columbia, PO Box 9452 Stn Prov Govt, Victoria 
 * BC, V8W 9V7, (250) 387-3309 if you have any questions or have received this class in 
 * error.
 * 
 */
package gov.ca.bc.qp.QPDefender.web;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import gov.ca.bc.qp.qpcommon.dom.DOMUtil;

/**
 * An object for holding common messages to return to the client for the QPDefender project.
 * @author spencer.tickner
 */
public enum Message {

	SUCCESS("Success"),
	FAILURE("Failure"),
	UNMATCHED_CREDENTIALS("Credentials don't match"),
	USER_NOT_FOUND("User Not Found"),
	CREDENTIALS_TO_SHORT("Credentials are not long enough");

	// We have an exception we don't handle,, we must log this.
	Logger logger = LogManager.getLogger(this.getClass());
	
	// Private member variable
	private Document docMessage = null;
	
	/**
	 * Constructor for creating a new message.
	 * @param message	The String message to wrap in our returning document.
	 */
	private Message(String message) {
		// Create an empty document.
		Document doc = null;
		try {
			doc = DOMUtil.getEmptyDocument();
		} catch (ParserConfigurationException ignore) {
			// This should never happen. Log the error.
			logger.error("Unhandled exception creating a new empty document for enum Message", ignore);
		}
		/*
		 *  Our wrapper for messages looks like:
		 *  <?xml version="1.0"?>
		 *  <root>
		 *  	<message>Whatever the message is</message>
		 * 	</root>
		 */
		Element root = doc.createElement("root");
		Element emessage = doc.createElement("message");
		emessage.setTextContent(message);
		root.appendChild(emessage);
		doc.appendChild(root);
		// Set our private member variable.
		this.docMessage = doc;
	}
	
	/**
	 * @return A w3c document representation of our dom wrapped message.
	 */
	public Document getMessage() {
		return this.docMessage;
		
	}
	
	
	
}
