package gov.ca.bc.qp.QPDefender.web;

import gov.ca.bc.qp.QPDefender.test.JerseyServerUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

import junit.framework.Assert;

import org.junit.Test;

/*import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.core.util.MultivaluedMapImpl;*/


public class WebGroupJerseyTest {
	/* this class has been commented out since it has been using Jersey 1.x 
	 * in case this class is needed, simply uncomment and convert to apis compatible to Jersey 2.x */
	/*
	 * static Client client = Client.create(); static String URL_LOGIN =
	 * "http://localhost:8080/QPDefender/j_security_check"; String username =
	 * "username"; String password = "pw";
	 * 
	 * 
	 * @Test public void testJerseyServerUtil() { JerseyServerUtil server = null;
	 * try { server = new JerseyServerUtil("stickner", "testing",
	 * "http://localhost:8080/QPDefender/app/none/groups"); } catch (IOException e)
	 * { e.printStackTrace(); Assert.fail(); } ClientResponse response =
	 * server.getResponse("ID/1");
	 * System.out.println(response.getEntity(String.class)); server.reset(); }
	 * 
	 * 
	 * public void setup() { client.addFilter(new ClientFilter() {
	 * 
	 * private ArrayList<Object> cookies;
	 * 
	 * public ClientResponse handle(ClientRequest request) throws
	 * ClientHandlerException {
	 * 
	 * if(cookies != null) { request.getHeaders().put("Cookie", cookies); }
	 * 
	 * ClientResponse response = getNext().handle(request); if(response.getCookies()
	 * != null) { if(cookies == null) { cookies = new ArrayList<Object>(); }
	 * cookies.addAll(response.getCookies()); } return response; } }); // First we
	 * ping the authenticator to create a session. WebResource service =
	 * client.resource(UriBuilder.fromUri(
	 * "http://localhost:8080/QPDefender/app/none/groups").build()); ClientResponse
	 * response = service.path("ID/1").get(ClientResponse.class);
	 * //System.out.println(response.getEntity(String.class));
	 * 
	 * WebResource resource = client.resource(URL_LOGIN);
	 * com.sun.jersey.api.representation.Form form = new Form();
	 * form.putSingle("j_username", username); form.putSingle("j_password",
	 * password); resource.type("application/x-www-form-urlencoded").post(form);
	 * 
	 * //service = client.resource(UriBuilder.fromUri(
	 * "http://localhost:8080/QPDefender/app/none/groups").build()); response =
	 * service.path("ID/1").get(ClientResponse.class);
	 * System.out.println(response.getEntity(String.class)); }
	 */
	/*
	private WebResource resource(String path) {
		return 
	}
	
	private Client createClient() {
		ClientConfig clientConfig = new DefaultClientConfig();
		return Client.create(clientConfig);
	}
	*/
}
