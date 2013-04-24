package gov.ca.bc.qp.QPDefender.DAO;

/**
 * A character was found that is invalid with regards to data constraints.
 * @author spencer.tickner
 *
 */
public class InvalidCharacterException extends Exception {

	/**
	 * Invalid character was found.
	 * @param message Descriptive message detailing the exception.
	 */
	public InvalidCharacterException(String message) {
		super(message);
	}
}
