package gov.ca.bc.qp.QPDefender.beans;

public class UnknownCredentialException extends Exception {

	public UnknownCredentialException(String message) {
		super(message);
	}
	
	public UnknownCredentialException(Exception e) {
		super(e);
	}
}
