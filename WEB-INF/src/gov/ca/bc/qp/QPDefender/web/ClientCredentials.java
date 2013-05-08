package gov.ca.bc.qp.QPDefender.web;

import java.security.Principal;

import org.apache.http.auth.Credentials;

public class ClientCredentials implements Credentials {
	
	private Principal principal;
	
	public ClientCredentials(Principal principal) {
		this.principal = principal;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return "xxxxxxxxxxx";
	}

	@Override
	public Principal getUserPrincipal() {
		// TODO Auto-generated method stub
		return principal;
	}

}
