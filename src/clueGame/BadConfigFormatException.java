package clueGame;

/**
 * Custom exception thrown when configuration files have invalid format
 * This includes setup files and layout files that don't match expected format
 */
public class BadConfigFormatException extends Exception {
	
	/**
	 * Default constructor with generic error message
	 */
	public BadConfigFormatException() {
		super("Bad configuration file format detected");
	}
	
	/**
	 * Constructor with custom error message
	 * @param message the detailed error message
	 */
	public BadConfigFormatException(String message) {
		super(message);
	}
}
 
