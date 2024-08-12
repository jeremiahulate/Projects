package crs;

/**
 * Provides methods in common between clients, either admin or student
 * clients.
 * 
 * @author Christopher Harris
 * @version 1.0
 */
public interface Client {

	// The minimum option a user can choose
	final int MIN_OPTION = 0;

	// The exit option
	final int EXIT = 0;

	// The help option
	final int HELP = 1;

	// The option to print the current schedule
	final int PRINT_SCHEDULE = 2;

	// The option to add a course
	final int ADD_COURSE = 3;

	// The option to drop a course
	final int DROP_COURSE = 4;

	// The option to search for a course
	final int SEARCH_COURSE = 5;

	// The option to check the current security level
	final int CHECK_SECURITY = 6;

	// The option to elevate to sysadmin
	final int ELEVATE = 7;

	// The option to link a lab and a lecture together
	final int LINK_LAB_LECTURE = 8;

	// The exit code for success
	final int SUCCESS = 0;

	// The exit code for a fatal error
	final int FATAL = 1;

	// The exit code for a nonfatal error
	final int NONFATAL_ERROR = -1;

	// The exit code for a fatal error that happened because a method was undefined
	final int FATAL_UNDEFINED = -2;
	
	/**
	 * Signals to the main class that the program needs to exit
	 * @param error_code The code to exit with
	 */
	void exitWithCode(int error_code);

	/**
	 * Prints the list of options available
	 */
	void getOptions();

	/**
	 * Code that is executed on program exit
	 */
	void onExit();
	
	/**
	 * Method to add a course to the schedule
	 */
	void addCourse();

	/**
	 * Method to drop a course from the schedule
	 */
	void dropCourse();

	/**
	 * Method to search for a course in the registry
	 */
	void searchCourse();

	/**
	 * Prints the user's current schedule
	 */
	void printSchedule();

	/**
	 * Gets user input
	 * @return The option the user chooses
	 */
	int getInput();

}