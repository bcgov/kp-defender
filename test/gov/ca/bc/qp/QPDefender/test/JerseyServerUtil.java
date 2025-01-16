package gov.ca.bc.qp.QPDefender.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

/*import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.api.representation.Form;*/

public class JerseyServerUtil {
	/* this class has been commented out since it has been using Jersey 1.x 
	 * in case this class is needed, simply uncomment and convert to apis compatible to Jersey 2.x */
	 
	/*
	 * private enum METHOD { POST, GET }
	 * 
	 * private static final Pattern security_pattern =
	 * Pattern.compile("^(http://[^/]+)/([^/]+)/"); // Should not be called
	 * directly, use the getClient method to return an authorized client.
	 * WebResource service = null;
	 * 
	 * // Private member variables private String username = ""; private String
	 * password = ""; private String basePath = ""; private String urlLogin = "";
	 * 
	 *//**
		 * Constructor for setting authentication for our client to use to access Jersey
		 * services.
		 * 
		 * @param username Human readable name identifying our user.
		 * @param password Password for authenticating this user.
		 * @param basePath Fully qualified path to the Jersey Bean. Ex:
		 *                 http://localhost:8080/QPDefender/app/none/group
		 * @throws IOException If the basePath does not meet our expected pattern.
		 */
	/*
	 * public JerseyServerUtil(String username, String password, String basePath)
	 * throws IOException { this.username = username; this.password = password;
	 * this.basePath = basePath; Matcher m = security_pattern.matcher(basePath);
	 * if(m.find()) { urlLogin = m.group(1) + "/" + m.group(2) +
	 * "/j_security_check"; } else { throw new
	 * java.io.IOException("Invalid basePath"); } }
	 * 
	 *//**
		 * Singleton interface that creates a client that's authenticated for reuse on
		 * subsequent tests.
		 * 
		 * @param path Path following the path to the Jersey Bean Ex: ID/1
		 * @return An authorized client for subsequent requests.
		 */
	/*
	 * private WebResource getService(String path, METHOD method) { if(service ==
	 * null) { Client client = Client.create(); client.addFilter(new ClientFilter()
	 * {
	 * 
	 * private ArrayList<Object> cookies;
	 * 
	 * public ClientResponse handle(ClientRequest request) throws
	 * ClientHandlerException {
	 * 
	 * if(cookies != null) { request.getHeaders().put("Cookie", cookies); }
	 * ClientResponse response = getNext().handle(request); if(response.getCookies()
	 * != null) { if(cookies == null) { cookies = new ArrayList<Object>(); }
	 * cookies.addAll(response.getCookies()); } return response; } }); // First we
	 * ping the authenticator to create a session. service =
	 * client.resource(UriBuilder.fromUri(basePath).build()); if(method ==
	 * METHOD.GET) { service.path(path).get(ClientResponse.class); } else {
	 * service.path(path).post(); }
	 * 
	 * // Now we create a login to pass to j_security_check with our session to
	 * authenticate our user. WebResource resource = client.resource(this.urlLogin);
	 * com.sun.jersey.api.representation.Form form = new Form();
	 * form.putSingle("j_username", username); form.putSingle("j_password",
	 * password); resource.type("application/x-www-form-urlencoded").post(form);
	 * 
	 * //service = client.resource(UriBuilder.fromUri(
	 * "http://localhost:8080/QPDefender/app/none/groups").build());
	 * 
	 * } return service; }
	 * 
	 *//**
		 * Executes a GET request to a Jersey service.
		 * 
		 * @param pathSnippet Path to the service we wish to consume under the basepath.
		 *                    EX: ID/2
		 * @return response from the request.
		 */
	/*
	 * public ClientResponse getResponse(String pathSnippet) { WebResource s =
	 * this.getService(pathSnippet, METHOD.GET); return
	 * s.path(pathSnippet).get(ClientResponse.class);
	 * //System.out.println(response.getEntity(String.class)); }
	 * 
	 *//**
		 * Executes a POST request to a Jersey service.
		 * 
		 * @param pathSnippet Path to the service we wish to consume under the basepath.
		 *                    EX: ID/2
		 * @return response from the request.
		 *//*
			 * public ClientResponse postResponse(String pathSnippet, MultivaluedMap<String,
			 * String> formData) { return this.getService(pathSnippet,
			 * METHOD.POST).path(pathSnippet).type(MediaType.
			 * APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class, formData); }
			 * 
			 * public void reset() { this.service = null; }
			 */
}
