package crs;

import java.util.Scanner;

import crs.client.SecureClient;
import crs.course.CourseRegistry;

/**
 * @author Christopher Harris
 * @author Jason Tran
 * @author Jeremiah Ulate
 * @version 1.0
 */
public class Main {
	
	// The client that is loaded by the main method
	public static SecureClient client;
	
	// The arguments handler that the main method uses
	public static CRSArgumentsHandler args_handler;
	
	// The error handler that the main method uses
	public static CRSErrorHandler error_handler;
	
	// The scanner used for user input
	public static Scanner user_input;
	
	// The course registry used by the whole program
	public static CourseRegistry course_registry;
	
	/**
	 * The main method
	 * @param args The arguments provided by the user
	 */
	public static void main(String[] args) {
		user_input = new Scanner(System.in);
		
		args_handler = new CRSArgumentsHandler();
		error_handler = new CRSErrorHandler();
		
		client = args_handler.loadClientWithArgs(args);
		System.out.println();
		
		if (client == null) {
			System.err.println("|DEBUG|+/ main(args: String[]): void|: Client is undefined!");
			System.exit(-1);
		}
		
		while (client.return_code != Client.SUCCESS || client.return_code != Client.FATAL) {
			client.run();
			client.onExit();
			
			if (client.return_code == Client.SUCCESS) {
				System.out.println("Execution completed succesfully...");
				user_input.close();
				System.exit(0);
			} else if (isNonFatal(client.return_code)) {
				String error = error_handler.getError(client.return_code);
				if (error == null) {
					System.err.println("|ERROR SQUARED|: Undefined error code: " + client.return_code);
					client.return_code = Client.FATAL;
				} else {
					System.err.println("|DEBUG|ERROR|: +/ Main.main(args: String[]): void -> Nonfatal error reported (code: " + client.return_code + ")");
					System.err.println(error);
				}
			}
		}
		
		if (client.return_code != Client.NONFATAL_ERROR || client.return_code == Client.SUCCESS) {
			user_input.close();
			System.exit(client.return_code);
		} else {
			user_input.close();
			System.out.println("Nonfatal error reported");
		}
	}
	
	/**
	 * Checks whether or not a given return code is non-fatal
	 * @param return_code The return code to check
	 * @return True if the return code is non-fatal, false if it is fatal
	 */
	public static boolean isNonFatal(int return_code) {
		return return_code <= 0;
	}
}