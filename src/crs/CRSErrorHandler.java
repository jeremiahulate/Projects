package crs;

/**
 * Provides a method for printing error messages based on a specific
 * error code
 * 
 * @author Christopher Harris
 * @version 1.0
 */
public class CRSErrorHandler {

	/**
	 * Returns an error message based on the provided error code
	 * @param error_code The error code that was thrown
	 * @return The error message
	 */
	public String getError(int error_code) {
		switch(error_code) {
		default:
			System.err.println("Unknown error code: " + error_code);
			return null;
		}
	}
	
}
