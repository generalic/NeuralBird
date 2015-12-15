package hr.fer.zemris.gen.exceptions;

/**
 * This exception is thrown to indicate that value for fitness was not calculated for requested object. Therefore, no
 * valid value can be returned.
 * 
 * @author Domagoj Latečki
 * @version 1.0
 */
public class FitnessNotEvaluatedException extends RuntimeException {
	
	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs an <code>FitnessNotEvaluatedException</code> with no detail message.
	 */
	public FitnessNotEvaluatedException() {}
	
	/**
	 * Constructs an <code>FitnessNotEvaluatedException</code> with specified detail message.
	 * 
	 * @param message error message of the exception.
	 */
	public FitnessNotEvaluatedException(String message) {
		super(message);
	}
}