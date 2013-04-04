/*
 * Copyright (c) 2013, Queen's Printer of British Columbia, Canada and/or its affiliates. 
 * All rights reserved. DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE Header.
 * 
 * Please contact Queen's Printer of British Columbia, PO Box 9452 Stn Prov Govt, Victoria 
 * BC, V8W 9V7, (250) 387-3309 if you have any questions or have received this class in 
 * error.
 * 
 */
package gov.ca.bc.qp.QPDefender.config;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import gov.ca.bc.qp.qpcommon.authenticate.QPPrincipal;
import gov.ca.bc.qp.qpcommon.dom.XSLTResolver;

/**
 * Resolves xslt paths from our REST interface based on the rules intrinsic to the
 * 	QPDefender project.
 * @author spencer.tickner
 * @see XSLTResolver
 */
public class MyResolver implements XSLTResolver {
	
	private static final Pattern XSL_PATTERN = Pattern.compile("[^/]+");
	private static final Pattern PARAM_PATTERN = Pattern.compile("([^=]+)=(.+)");
	public static final String ROLE_PARAM_NAME = "roles";
	
	/**
	 * Keyword for not doing a xslt transformation.
	 */
	public static final String NO_TRANSFORM = "none";
			
	// Private member variables
	private URL url = null;
	private Source source = null;
	private Map<String, String> params = null;
	
	
	/**
	 * Constructor for resolving URL and parameters for our Defender XSLT stylsheets.
	 * @param xsltPath	The unresolved path to our xslt in the format "xsl" or in the form
	 * 						"xsl/param1=value1/param2=value2" in the case that path parameters
	 * 						are being passed in as well.
	 * @param principal	By default the roles that the user belongs to are always passed to
	 * 						the stylsheet. The principal allows us to get access to these roles.
	 */
	public MyResolver(String xsltPath, QPPrincipal principal) {
		// OK, expect path is "xsl/param1=value1/param2=value2", or without params
		//	it would be "xsl", change the xsl into a URL and source, change the params into
		//	a map of parameters. We are going to default to always adding the role of the user
		//	to the params as well.
		
		
		// If the xsltPath is our no transfomation keyword don't go to the trouble
		if(xsltPath.equals(MyResolver.NO_TRANSFORM)) {
			Source source = new StreamSource();
			source.setSystemId(MyResolver.NO_TRANSFORM);
			this.setSource(source);
			this.setURL(null);
			
		} else {
			// start with adding our roles to our parameters.
			if(principal != null) { // Kill null pointers.
				List<String> roles = principal.getRoles();
				if(roles != null && roles.size() > 0) {
					params = new HashMap<String, String>();
					params.put(MyResolver.ROLE_PARAM_NAME, this.convertListToCommaString(roles));
				}
			}
			
			//	Next create a matcher. 1st match represents the xsl, each subsequent match represents
			//		a name value parameter pair seperated by an equal sign.
			Matcher m = MyResolver.XSL_PATTERN.matcher(xsltPath);
			boolean bPath = true;
			while(m.find()) {
				// First match is our path.
				if(bPath) {
					bPath = false; // Ensure subsequent matches are handled as parameters.
					String unResolvePath = m.group(0); // get our unresolved path.
					if(!unResolvePath.startsWith("/"))
						unResolvePath = "/" + unResolvePath;
					// Create our path
					unResolvePath = "/xsl" + unResolvePath + ".xsl";
					// Set our member variables.
					this.setURL(MyResolver.class.getResource(unResolvePath));
					File xsl = this.getFile(this.getURL());
					this.setSource(new StreamSource(xsl));
				} else {
					// Each subsequent match is parameters.
					Matcher mParam = MyResolver.PARAM_PATTERN.matcher(m.group(0));
					if(mParam.matches()) {
						// Kill null pointer exceptions
						if(params == null)
							params = new HashMap<String, String>();
						// Add our param groups.
						params.put(mParam.group(1), mParam.group(2));
					}
					
				}
				
			}
		
		}

	}

	@Override
	public URL getURL() {
		return this.url;
	}

	@Override
	public Source getSource() {
		return this.source;
	}
	
	@Override
	public Map<String, String> getParams() {
		return this.params;
	}
	
	/**
	 * @param url The url of our XSLT stylesheet.
	 */
	private void setURL(URL url) {
		this.url = url;
	}
	
	/**
	 * @param source The source of our XSLT stylesheet.
	 */
	private void setSource(Source source) {
		this.source = source;
	}
	
	/**
	 * Helper method for converting a url to a file and handling some of the quirky url
	 * 	syntax issues
	 * @param url	the url to convert to a file.
	 * @return	A file based on the url.
	 */
	private File getFile(URL url) {
		File f = null;
		try {
			f = new File(url.toURI());
		} catch (URISyntaxException e) {
			f = new File(url.getPath());
		}
		return f;
	}
	
	/**
	 * Helper method for converting a list of strings to a single comma separated String.
	 * @param list The list of strings to convert to a comma separated string.
	 * @return comma separated string.
	 */
	private String convertListToCommaString(List<String> list) {
		StringBuilder sb = new StringBuilder();
		for(String str : list) {
			sb.append(str).append(",");
		}
		// remove the trailing string.
		if(list.size() > 0) {
			sb.delete(sb.length()-1,  sb.length());
		}
		return sb.toString();
	}

}
