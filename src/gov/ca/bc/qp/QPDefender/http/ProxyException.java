package gov.ca.bc.qp.QPDefender.http;

public class ProxyException extends Exception {

	public ProxyException(Exception e) {
		super(e);
	}
	public ProxyException(String message) {
		super(message);
	}
}
