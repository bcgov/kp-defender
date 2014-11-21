package gov.ca.bc.qp.QPDefender.web;


import java.util.Iterator;

import javax.annotation.security.PermitAll;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.catalina.authenticator.Constants;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;


@Path("client")
public class ClientTest {

	@Context SecurityContext sc;
	@Context ServletContext servlet;
	@Context HttpHeaders hh;
	@Context HttpServletRequest c;
	
	
	@GET
	@PermitAll
	@Path("test")
	public Response test() throws Exception {

		HttpClient httpclient = new DefaultHttpClient();
		CookieStore cookieStore = new BasicCookieStore();
		HttpContext localContext = new BasicHttpContext();
	/*
		javax.servlet.http.Cookie cookies[] = c.getCookies();
		for(int i = 0; i < cookies.length; i++) {
			BasicClientCookie cook = new BasicClientCookie(cookies[i].getName(), cookies[i].getValue());
			cook.setComment(cookies[i].getComment());
			cook.setSecure(cookies[i].getSecure());
	    	cook.setDomain(cookies[i].getDomain());
	    	cook.setVersion(cookies[i].getVersion());
	    	cook.setPath(cookies[i].getPath());
	    	cookieStore.addCookie(cook);
		
		}
		c.getH
		*/
	    Iterator<String> ckeys = hh.getCookies().keySet().iterator();

	    while(ckeys.hasNext()) {
	    	String ckey = ckeys.next();
	    	javax.ws.rs.core.Cookie clientCookie = hh.getCookies().get(ckey);
	    	BasicClientCookie cook = new BasicClientCookie(clientCookie.getName(), clientCookie.getValue());
	    	cook.setDomain(clientCookie.getDomain());
	    	cook.setVersion(clientCookie.getVersion());
	    	cook.setPath(clientCookie.getPath());
	    	cookieStore.addCookie(cook);
	    }

		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		
		localContext.setAttribute(Constants.FORM_PRINCIPAL_NOTE, sc.getUserPrincipal());
		Credentials creds = new ClientCredentials(sc.getUserPrincipal());
		((DefaultHttpClient)httpclient).getCredentialsProvider().setCredentials(AuthScope.ANY, creds);
		HttpGet httpget = new HttpGet("http://localhost:8080/QPDefender/app/none/groups/me");
		
	    Iterator<String> keys = hh.getRequestHeaders().keySet().iterator();
	    while(keys.hasNext()) {
	    	String key = keys.next();
	    	String value = hh.getRequestHeaders().get(key).get(0);
	    	httpget.setHeader(key, value);
	    	//service.header(key, value);
	    }
		
		
		
		HttpResponse httpRes = httpclient.execute(httpget, localContext);
		Response response = Response.ok().entity(httpRes.getEntity().getContent()).build();
		
		
		return response;
		
		/*
		
		SecurityContextFilter filter = new SecurityContextFilter(sc);
	
		javax.servlet.RequestDispatcher dispatcher = re.getRequestDispatcher("/QPDefender/app/none/groups/me");

		Session session = (Session) c.getSession();
		Principal principal = sc.getUserPrincipal();
		Request request = new Request();
		session.setNote(Constants.FORM_PRINCIPAL_NOTE, principal);
		session.setNote(Constants.SESS_USERNAME_NOTE, principal.getName());
		session.setNote(Constants.SESS_PASSWORD_NOTE, "");
		request.setUserPrincipal(principal);

		
		
	    Client client = Client.create();
	    //client.addFilter(new RolesAllowedResourceFilterFactory());
	    WebResource service = client.resource(UriBuilder.fromUri("http://localhost:8080/QPDefender/app/none/groups/me").build());

	    Iterator<String> keys = hh.getRequestHeaders().keySet().iterator();
	    while(keys.hasNext()) {
	    	String key = keys.next();
	    	String value = hh.getRequestHeaders().get(key).get(0);
	    	service.header(key, value);
	    }
	    Iterator<String> ckeys = hh.getCookies().keySet().iterator();
	    while(ckeys.hasNext()) {
	    	String ckey = ckeys.next();
	    	service.cookie(hh.getCookies().get(ckey));
	    }
	    
	    
	    ClientResponse cr = service.get(ClientResponse.class);//.post(ClientResponse.class, exam);

	    Response response = null;
	    if(cr.getStatus() == 200) {
	    	String doc = cr.getEntity(String.class);
	    	response = Response.ok().entity(doc).build();
	    } else {
	    	response = Response.ok().entity(cr.getStatus()).build();
	    }
	    return response;
	    */
	}
}
