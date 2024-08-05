package crs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import crs.client.AdminClient;
import crs.client.SecureClient;
import crs.client.StudentClient;
import crs.client.filehandling.FileHandler;
import crs.course.CourseRegistry;
import crs.security.CRSSecurity;
import crs.security.Cipher;
import crs.security.Crypto;

/**
 * Contains a method for loading a client based on the main method's
 * arguments provided by the user on program execution
 * 
 * @author Christopher Harris
 * @version 1.0
 */
public class CRSArgumentsHandler {
	
	// The parameters provided to the program via the main method's arguments
	public static final Map<String, List<String>> parameters = new HashMap<>();
	
	/**
	 * Loads a client based on the arguments provided by main method
	 * @param args The arguments provided by the main method
	 * @return Either an AdminClient or a StudentClient, depending on the user key provided
	 */
	public SecureClient loadClientWithArgs(String[] args) {
		CRSArgumentsHandler.loadParameters(args);
		
		FileHandler.addFile("prereqs", CRSArgumentsHandler.getParameter("p", "-prereq", 0));
		FileHandler.addFile("registry", CRSArgumentsHandler.getParameter("r", "-registry", 0));
		
		Main.course_registry = CourseRegistry.loadRegistryFromFile(CRSArgumentsHandler.getParameter("r", "-registry", 0));
		
		Crypto crypto = new Cipher();
		
		if (getOption("u", "-key")) {
			if (CRSSecurity.validateKey(crypto, CRSArgumentsHandler.getParameter("u", "-key", 0), CRSSecurity.KEY_LENGTH)) {
				return AdminClient.loadClient(args);
			}
		}
		
		return StudentClient.loadClient(args);
	}
	
	/**
	 * Populates the static HashMap 'parameters'
	 * @param args The arguments provided by the main method
	 */
	public static void loadParameters(String[] args) {
		List<String> options = null;
		for (int i = 0; i < args.length; i++) {
			final String arg = args[i];
			
			if (arg.charAt(0) == '-') {
				if (arg.length() < 2) {
					System.err.println("Error at argument " + arg);
					return;
				}
				
				options = new ArrayList<>();
				parameters.put(arg.substring(1), options);
			} else if (options != null) {
				options.add(arg);
			} else {
				System.err.println("Illegal parameter usage");
				return;
			}
		}
	}
	
	/**
	 * Gets whether or not the argument provided by key, with alias
	 * defined by alias_key, is in the HashMap 'parameters'
	 * @param key The single character flag
	 * @param alias_key The multi-character name of the key
	 * @return True if the flag is present in the arguments, false if not
	 */
	public static boolean getOption(String key, String alias_key) {
		return parameters.containsKey(key) || parameters.containsKey(alias_key);
	}
	
	/**
	 * Gets the value of the argument provided by key, with alias
	 * defined by alias_key, in the HashMap 'parameters'
	 * @param key The single character flag
	 * @param alias_key The multi-character name of the key
	 * @param index Which parameter to get
	 * @return The value of the argument requested
	 */
	public static String getParameter(String key, String alias_key, int index) {
		if (parameters.containsKey(key)) {
			if (index < parameters.get(key).size())
				return parameters.get(key).get(index);
			else {
				System.err.println("Error in getParameter(" + key + ", " + alias_key + ", " + index + ")");
				return null;
			}
		} else if (parameters.containsKey(alias_key)) {
			if (index < parameters.get(alias_key).size())
				return parameters.get(alias_key).get(index);
			else {
				System.err.println("Error in getParameter(" + key + ", " + alias_key + ", " + index + ")");
				return null;
			}
		} else {
			return null;
		}
	}
}