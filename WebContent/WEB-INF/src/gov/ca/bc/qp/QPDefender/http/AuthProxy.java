package gov.ca.bc.qp.QPDefender.http;

import gov.ca.bc.qp.QPDefender.web.ClientCredentials;
import gov.ca.bc.qp.qpcommon.authenticate.QPPrincipal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.security.Principal;
import java.util.Iterator;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.catalina.authenticator.Constants;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.ContextAwareAuthScheme;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

public class AuthProxy {
	
/*
	public static Source authxxxSource(URI uri, HttpHeaders header, final Principal principal) throws ProxyException {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		// Pre-emptive authentication to speed things up
		BasicHttpContext localContext = new BasicHttpContext();

		BasicScheme basicAuth = new BasicScheme();
		localContext.setAttribute("preemptive-auth", basicAuth);

		Credentials creds = new Credentials() {
			@Override
			public String getPassword() {
				// TODO Auto-generated method stub
				return "xxxxxxxxxx";
			}

			@Override
			public Principal getUserPrincipal() {
				// TODO Auto-generated method stub
				return principal;
			}
			
		};
		httpclient.getCredentialsProvider().setCredentials(AuthScope.ANY, creds);
		httpclient.addRequestInterceptor(new PreemptiveAuthInterceptor(), 0);
		
		HttpGet httpget = new HttpGet(uri);
		
		HttpResponse httpRes = null;
		Source source = null;
		
		try {
			httpRes = httpclient.execute(httpget, localContext);
			source = new StreamSource(httpRes.getEntity().getContent());
		} catch (ClientProtocolException e) {
			throw new ProxyException(e);
		} catch (IOException e) {
			throw new ProxyException(e);
		}
		
		return source;
	}
	
	static class PreemptiveAuthInterceptor implements HttpRequestInterceptor {
		@Override
		public void process(final HttpRequest request, final HttpContext context)
				throws HttpException, IOException {
			AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);
			if(authState.getAuthScheme() == null) {
	            CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(ClientContext.CREDS_PROVIDER);
	            HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
	            Credentials creds = credsProvider.getCredentials(new AuthScope(targetHost.getHostName(), targetHost.getPort()));
	            if (creds == null)
	                throw new HttpException("No credentials for preemptive authentication");
	            authState.update(new BasicScheme(), creds);
	            //authState.setCredentials(creds);
			}
			
			
		}
		
	}
*/
	
	public static Source getSource(URI uri, final Principal principal) throws ProxyException {
		
		HttpClient httpclient = new DefaultHttpClient();
		//CookieStore cookieStore = new BasicCookieStore();
		//HttpContext localContext = new BasicHttpContext();
		QPPrincipal qpp = (QPPrincipal)principal;
		
		HttpResponse httpRes = null;
		Source source = null;
		/*
	    Iterator<String> ckeys = header.getCookies().keySet().iterator();
	    while(ckeys.hasNext()) {
	    	String ckey = ckeys.next();
	    	javax.ws.rs.core.Cookie clientCookie = header.getCookies().get(ckey);
	    	BasicClientCookie cook = new BasicClientCookie(clientCookie.getName(), clientCookie.getValue());
	    	cook.setDomain(clientCookie.getDomain());
	    	cook.setVersion(clientCookie.getVersion());
	    	cook.setPath(clientCookie.getPath());
	    	cookieStore.addCookie(cook);
	    }
	    
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		
		localContext.setAttribute(Constants.FORM_PRINCIPAL_NOTE, principal);
		*/
		/*
		Credentials creds = new Credentials() {
				@Override
				public String getPassword() {
					// TODO Auto-generated method stub
					return "xxxxxxxxxx";
				}
	
				@Override
				public Principal getUserPrincipal() {
					// TODO Auto-generated method stub
					return principal;
				}
				
			};
		((DefaultHttpClient)httpclient).getCredentialsProvider().setCredentials(AuthScope.ANY, creds);
		*/
		
		HttpGet httpget = new HttpGet(uri);
		//httpget..setHeader(Constants.FORM_PRINCIPAL_NOTE, principal);
		/*
		try {
			httpget.addHeader(new BasicScheme().authenticate(creds, httpget));
		} catch (AuthenticationException e1) {
			throw new ProxyException(e1);
		}
		*/
		/*
	    Iterator<String> keys = header.getRequestHeaders().keySet().iterator();
	    while(keys.hasNext()) {
	    	String key = keys.next();
	    	String value = header.getRequestHeaders().get(key).get(0);
	    	httpget.setHeader(key, value);
	    	localContext.setAttribute(key, value);
	    }
	    */
	    
	    //localContext.setAttribute("system_access", qpp.getSessionID());
	    httpget.setHeader("system_access", qpp.getSessionID());
	    
		//httpget.setHeader(Constants.FORM_PRINCIPAL_NOTE, principal.getName());
		
		try {
			httpRes = httpclient.execute(httpget);
			//httpRes = httpclient.execute(httpget, localContext);
			// TODO: Delete below
			/*
			InputStream is = httpRes.getEntity().getContent();
			String testing = convertStreamToString(is);
			System.out.println(testing);
			*/
			// Done delete
			source = new StreamSource(httpRes.getEntity().getContent());
		} catch (ClientProtocolException e) {
			throw new ProxyException(e);
		} catch (IOException e) {
			throw new ProxyException(e);
		}
		
		return source;
	}
	
	public static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
}
