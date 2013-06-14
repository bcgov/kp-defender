package gov.ca.bc.qp.QPDefender.utility;

import javax.ws.rs.core.MediaType;
import javax.activation.MimetypesFileTypeMap;
public class QPMediaType {
	
	private static MimetypesFileTypeMap mimes = null;
	
	private static MimetypesFileTypeMap getMimes() {
		if(mimes == null) {
			mimes = new MimetypesFileTypeMap();
			mimes.addMimeTypes("application/msword doc dot wiz rtf");
			mimes.addMimeTypes("application/pdf pdf");
			mimes.addMimeTypes("application/postscript ai eps ps");
			mimes.addMimeTypes("application/vnd.ms-excel xls xlw xla xlc xlm xlt");
			mimes.addMimeTypes("application/vnd.ms-powerpoint ppt pps pot");
			mimes.addMimeTypes("application/x-javascript js");
			mimes.addMimeTypes("application/x-asap asp");
			mimes.addMimeTypes("application/x-latex latex");
			mimes.addMimeTypes("application/x-tar tar");
			mimes.addMimeTypes("application/x-texinfo texinfo texi");
			mimes.addMimeTypes("application/zip zip");
			mimes.addMimeTypes("text/css css");
			mimes.addMimeTypes("text/html htm html");
			mimes.addMimeTypes("text/plain txt");
			mimes.addMimeTypes("text/richtext rtx");
			mimes.addMimeTypes("text/xml xml");
			mimes.addMimeTypes("image/png png");
			mimes.addMimeTypes("image/jpeg jfif jfif-tbnl jpe jpeg jpg");
			mimes.addMimeTypes("image/gif gif");
			
			
		}
		return mimes;
	}

	public static String getMediaTypeFromPath(String path) {
		String mt = getMimes().getContentType(path);
		return mt;
		/*
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
		*/
	}
	
	public static void main(String[] args) {
		String test1 = "/QPDefender/lightbox.css";
		String test2 = "/QPDefender/img.jpg";
		String test3 = "/QPDefender/img.png";
		String test4 = "/QPDefender/index.html";
		
		System.out.println(getMediaTypeFromPath(test1));
		System.out.println(getMediaTypeFromPath(test2));
		System.out.println(getMediaTypeFromPath(test3));
		System.out.println(getMediaTypeFromPath(test4));
		
	}
}
