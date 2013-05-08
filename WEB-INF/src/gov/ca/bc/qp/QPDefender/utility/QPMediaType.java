package gov.ca.bc.qp.QPDefender.utility;

import javax.ws.rs.core.MediaType;

public class QPMediaType {

	public static MediaType getMediaTypeFromPath(String path) {
		MediaType type = MediaType.APPLICATION_OCTET_STREAM_TYPE;
		// Make this lower case to ensure or test are standardized.
		path = path.toLowerCase();
		if(path.endsWith(".css") || path.endsWith(".js")) {
			type = MediaType.TEXT_PLAIN_TYPE;
		} else if(path.endsWith(".html") || path.endsWith(".htm")) {
			type = MediaType.TEXT_HTML_TYPE;
		} else if(path.endsWith(".xml") || path.endsWith(".xslt")) {
			type = MediaType.TEXT_XML_TYPE;
		}
		return type;
	}
}
