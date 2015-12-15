package hr.fer.zemris.gen.exceptions;

/**
 * This exception is thrown to indicate that invalid number of parents has been provided in the crossover operator.
 * 
 * @author Domagoj Latečki
 * @version 1.0
 */
public class InvalidNumberOfParentsException extends RuntimeException {
	
	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs an <code>TooManyParentsException</code> with no detail message.
	 */
	public InvalidNumberOfParentsException() {}
	
	/**
	 * Constructs an <code>TooManyParentsException</code> with specified detail message.
	 * 
	 * @param message error message of the exception.
	 */
	public InvalidNumberOfParentsException(String message) {
		super(message);
	}
}