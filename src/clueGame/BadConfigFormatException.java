package clueGame;

public class BadConfigFormatException extends Exception {
	
	public BadConfigFormatException() {
		super("Bad configuration file format detected");
	}
	
	public BadConfigFormatException(String message) {
		super(message);
	}
}

