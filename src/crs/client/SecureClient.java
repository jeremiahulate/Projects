package crs.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;

import crs.Client;
import crs.Course;
import crs.Main;
import crs.SecurityLevel;

/**
 * A SecureClient with a particular SecurityLevel.
 * Contains information such as the user's schedule, and the
 * prerequisites of every course
 * 
 * @author Christopher Harris
 * @author Jason Tran
 * @author Jeremiah Ulate
 * @version 1.0
 */
public abstract class SecureClient implements Client {

	// The prerequisites of all the courses
	protected HashMap<String, List<Course>> prerequisites;

	// The user's schedule of courses
	protected ArrayList<Course> user_schedule = new ArrayList<Course>();
	
	// The user's key provided upon program execution
	protected String user_key;

	// The option the user has selected
	protected int option;
	
	// The maximum option the user may select
	protected int max_option;
	
	// The arguments handler used by the client
	public ArgumentsHandler arguments_handler;
	
	// The base security level of the client, defaults to no security permissions
	public SecurityLevel base_level = SecurityLevel.LEVEL_NONE;
	
	// The current security level of the client
	public SecurityLevel security_level;
	
	// The return code to exit with
	public int return_code;

	/**
	 * This constructor modifies the security level of the user based on the 
	 * user key provided
	 * @param user_key The user key
	 * @param base_level The base security level
	 */
	protected SecureClient(String user_key, SecurityLevel base_level) {
		this.base_level = base_level;
		this.user_key = user_key;

		security_level = this.base_level;
		security_level = calculateElevationChange(security_level, this.user_key);
	}

	/**
	 * Sets the new maximum option the user may select
	 * @param new_max_option The new maximum option
	 */
	protected void setOptionRange(int new_max_option) {
		this.max_option = new_max_option;
	}

	/**
	 * Calculuates the new security level based on the provided user key
	 * @param base_level The base security level
	 * @param user_key The provided user key
	 * @return A new security level based on the user key and the base level
	 */
	protected SecurityLevel calculateElevationChange(SecurityLevel base_level, String user_key) {
		if (user_key == "sysadmin") {
			return SecurityLevel.LEVEL_SYSADMIN;
		} else {
			return base_level;
		}
	}

	/**
	 * Prints a list of the actions the user may perform based on the 
	 * user's current security level
	 */
	@Override
	public void getOptions() {
		System.out.println("0.  Exit");
		System.out.println("1.  Help");

		setOptionRange(1);

		if (security_level == SecurityLevel.LEVEL_STUDENT) {
			System.out.println("2.  Print schedule");
			System.out.println("3.  Add course");
			System.out.println("4.  Drop course");
			System.out.println("5.  Search for course");

			setOptionRange(5);
		} else if (security_level == SecurityLevel.LEVEL_ADMIN) {
			System.out.println("2.  Print schedule");
			System.out.println("3.  Add course");
			System.out.println("4.  Drop course");
			System.out.println("5.  Search for course");
			System.out.println("6.  Check security level");
			System.out.println("7.  Elevate to sysadmin level");

			setOptionRange(7);
		} else if (security_level == SecurityLevel.LEVEL_SYSADMIN) {
			System.out.println("2.  Print schedule");
			System.out.println("3.  Add course");
			System.out.println("4.  Drop course");
			System.out.println("5.  Search for course");
			System.out.println("6.  Check security level");
			System.out.println("7.  Elevate to sysadmin level");
			System.out.println("8.  Link lecture and lab together");

			setOptionRange(8);
		}
	}

	/**
	 * This program is run just before the program exits
	 */
	@Override
	public void onExit() {
		System.out.println("Exiting...");
	}

	/**
	 * This method gets the user's option. It is only used for getting options.
	 * Do not use it for getting integer inputs for any other purpose.
	 */
	@Override
	public int getInput() {
		System.out.print("Option (1 to see available options): ");
		try {
			int input = Main.user_input.nextInt();
			if (input > max_option) {
				System.out.println("Highest available option is " + max_option);
				return getInput();
			} else if (input < 0) {
				System.out.println("Lowest available option is " + MIN_OPTION);
				return getInput();
			}

			return input;
		} catch (InputMismatchException e) {
			System.err.println("Error in +^SecureClient.getOption(): int");
			return -1;
		} catch (NoSuchElementException e) {
			System.err.println("Error in +^SecureClient.getOption(): int");
			return -1;
		} catch (IllegalStateException e) {
			System.err.println("Error in +^SecureClient.getOption(): int");
			return -1;
		}
	}

	/**
	 * The method that is called just after the client is created.
	 * Should loop by checking whether or not getInput() != 0
	 */
	public abstract void run();

}