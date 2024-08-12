package crs.client;

import crs.CRSArgumentsHandler;
import crs.SecurityLevel;

/**
 * Handles arguments by checking whether or not a user with
 * a specific security level can even used such arguments
 * 
 * @author Christopher Harris
 * @version 1.0
 */
public class ArgumentsHandler {

	// The security level of the user
	public static SecurityLevel security_level;

	/**
	 * The constructor of the ArgumentsHandler
	 * @param user_level Updates the security level of the user
	 */
	public ArgumentsHandler(SecurityLevel user_level) {
		ArgumentsHandler.security_level = user_level;
	}

	/**
	 * Gets a parameter, in a similar manner to CRSArgumentsHandler
	 * See CRSArgumentsHandler for specific information
	 * @param required_security_level
	 * @param key
	 * @param alias_key
	 * @param index
	 * @return
	 */
	public static String getParameter(SecurityLevel required_security_level, String key, String alias_key, int index) {
		if (required_security_level == SecurityLevel.LEVEL_NONE) {
			return CRSArgumentsHandler.getParameter(key, alias_key, index);
		} else if (required_security_level == SecurityLevel.LEVEL_STUDENT) {
			if (ArgumentsHandler.security_level != SecurityLevel.LEVEL_NONE) {
				return CRSArgumentsHandler.getParameter(key, alias_key, index);
			} else {
				System.err.println("User with security level " + ArgumentsHandler.security_level.toString()
						+ " is trying to access parameter that requires " + required_security_level.toString());
				return null;
			}
		} else if (required_security_level == SecurityLevel.LEVEL_ADMIN) {
			if (ArgumentsHandler.security_level == SecurityLevel.LEVEL_ADMIN
					|| ArgumentsHandler.security_level == SecurityLevel.LEVEL_SYSADMIN) {
				return CRSArgumentsHandler.getParameter(key, alias_key, index);
			} else {
				System.err.println("User with security level " + ArgumentsHandler.security_level.toString()
						+ " is trying to access parameter that requires " + required_security_level.toString());
				return null;
			}
		} else if (required_security_level == SecurityLevel.LEVEL_SYSADMIN
				&& ArgumentsHandler.security_level == SecurityLevel.LEVEL_SYSADMIN) {
			return CRSArgumentsHandler.getParameter(key, alias_key, index);
		} else {
			System.err.println("User with security level " + ArgumentsHandler.security_level.toString()
					+ " is trying to access parameter that requires " + required_security_level.toString());
			return null;
		}
	}

}