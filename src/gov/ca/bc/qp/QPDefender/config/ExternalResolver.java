package gov.ca.bc.qp.QPDefender.config;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.UriInfo;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gov.ca.bc.qp.qpcommon.authenticate.QPPrincipal;
import gov.ca.bc.qp.qpcommon.dom.DefaultResolver;

public class ExternalResolver extends DefaultResolver {
	// Pattern for resolving our external xslt paths
	private static final Pattern EXTERNAL_XSL_PATTERN = Pattern.compile("(.+\\.xsl[t]?)(.*)");
	private static final Pattern EXTERNAL_PARAM_PATTERN = Pattern.compile("/([^=]+)=([^/]+)");
	private static final Logger log = LogManager.getLogger(ExternalResolver.class);
	

	/**
	 * Resolves xslts that are external to the QPDefender project. These references should be absolute
	 * 			and are resolved to the calling domain if preceded with '/' or any external domain
	 * 			when preceded with a protocol (ie. http).
	 * @param xsltPath Absolute path to the external resource if preceded with '/' or any external domain
	 * 					when preceded with a protocol (ie. http). 
	 * 					<p>Any slashes after the .xsl[t] will be treated as 
	 * 					parameters split as name=value.</p>
	 * 					<p>So a xsltPath  /app/templates/style.xsl/foo=bar coming from 
	 * 						www.test.com will resolve to
	 * 					http://www.test.com/app/templates/style.xsl and be passed the parameter
	 * 					"foo" with the value set as "bar".</p>
	 * 					<p>if xsltPath is http://www.test.com/app/style.xsl it will be resolved to
	 * 						the requested http resource and passed no parameters</p>
	 * @param principal	The user representation of the entity resolving this resource.
	 * @param uriInfo	Contains information about the request that was made to resolve this resource.
	 * @param callingObject	The object that is making the call to resolve this resource.
	 * @throws IOException 
	 */
	public ExternalResolver(String xsltPath, QPPrincipal principal, UriInfo uriInfo, Object callingObject) throws IOException {
		// First invoke our DefaultResolver constructor with the no transform xslt keyword.
		//		this will set our private member variables but allow us to overided the 
		//		parameter and url resolution to this class.
		super(ExternalResolver.NO_TRANSFORM, principal, uriInfo, callingObject);
		log.debug("Resolving external xslt path: " + xsltPath);
		
		// Not sure why anyone would do it but if they pass in the No transformation heyword as an
		//	external stylesheet reference respect the non transormation.
		if(!xsltPath.equalsIgnoreCase(NO_TRANSFORM)) {	
			// Use regex to cut of and set any additional parameters.
			Matcher m = EXTERNAL_XSL_PATTERN.matcher(xsltPath);
			// If this is an unknown pattern throw an error.
			if(m.find()) {
				String resPath = m.group(1);
				String paramPath = m.group(2);
				// Resolve our parameters as laid out in our parameter pattern
				Matcher m_param = EXTERNAL_PARAM_PATTERN.matcher(paramPath);
				while(m_param.find()) {
					this.getParams().put(m.group(1), m.group(2));
				}
				
				Source source = null;
				// Now we determine if our xsltPath is an absolute reference or a protocol reference.
				if(resPath.startsWith("/")) {
					// Use our URIInfo class to resolve our
					this.setURL(uriInfo.getBaseUri().resolve(resPath).toURL());
				} else {
					// Assume that a valid url has been passed in.
					this.setURL(new URL(resPath));
				}
				source = new StreamSource(this.getURL().openStream());
				this.setSource(source);
			} else {
				throw new IOException("Unknown external xslt pattern, unable to resolve.");
			}
			

		}
		
		
	}
}
