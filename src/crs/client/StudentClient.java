package crs.client;

import java.util.HashMap;
import java.util.List;

import crs.CRSArgumentsHandler;
import crs.Client;
import crs.Course;
import crs.Main;
import crs.SecurityLevel;
import crs.client.filehandling.FileHandler;

/**
 * An instance of a SecureClient that has no admin privileges
 * As such, it can do things such as elevate its own security level,
 * and modify the linked lab of lectures and the linked lectures of labs.
 * 
 * The user of the AdminClient is also a student with their own schedule
 * 
 * @author Christopher Harris
 * @author Jason Tran
 * @author Jeremiah Ulate
 * @version 1.0
 */
public class StudentClient extends SecureClient {

	/**
	 * The constructor is private because an StudentClient should only
	 * be instantiated using the loadClient() method
	 * @param user_key The key provided by the user
	 * @param base_level The base security level before any elevation change
	 */
	private StudentClient(String user_key, SecurityLevel base_level) {
		super(user_key, base_level);
	}
	
	/**
	 * See SecureClient.calculateElevationChange()
	 */
	@Override
	protected SecurityLevel calculateElevationChange(SecurityLevel base_level, String user_key) {
		if (base_level != SecurityLevel.LEVEL_NONE) {
			return SecurityLevel.LEVEL_STUDENT;
		} else {
			return base_level;
		}
	}

	/**
	 * Loads the prerequisites for each class
	 * @return A HashMap of all the prerequisites for each course
	 */
	private HashMap<String, List<Course>> loadPreReq() {
		return FileHandler.getPrereqs();
	}

	/**
	 * Loads a StudentClient based on the arguments provided by the main method
	 * @param args The arguments provided by the main method
	 * @return A new instance of StudentClient
	 */
	public static StudentClient loadClient(String args[]) {
		String user_key = CRSArgumentsHandler.getParameter("k", "-key", 0);
		SecurityLevel base_level = SecurityLevel.LEVEL_NONE;

		StudentClient client = new StudentClient(user_key, base_level);

		client.arguments_handler = new ArgumentsHandler(client.security_level);

		client.prerequisites = client.loadPreReq();
		
		System.out.println("Welcome, Student");

		return client;
	}

	/**
	 * See SecureClient.addCourse()
	 */
	@Override
	public void addCourse() {
		System.out.print("Enter CRN: ");
		int crn = Main.user_input.nextInt();
		Course course = Main.course_registry.searchCourse(crn);
		if (course != null) {
			user_schedule.add(course);
			course.seats--;
		} else {
			System.out.println("No course found matching CRN " + crn);
		}
	}

	/**
	 * See SecureClient.dropCourse()
	 */
	@Override
	public void dropCourse() {
		System.out.print("Enter CRN: ");
		int crn = Main.user_input.nextInt();
		Course course = Main.course_registry.searchCourse(crn);
		if (course != null) {
			if (user_schedule.contains(course)) {
				user_schedule.remove(course);
				course.seats++;
			}
		} else {
			System.out.println("No course found matching CRN " + crn);
		}
	}

	/**
	 * See SecureClient.searchCourse()
	 */
	@Override
	public void searchCourse() {
		System.out.print("Enter CRN: ");
		int crn = Main.user_input.nextInt();
		Course course = Main.course_registry.searchCourse(crn);
		if (course != null)
			System.out.println("Found course\n" + course.stringRepresentation());
		else
			System.out.println("No course found matching CRN " + crn);
	}
	
	/**
	 * See SecureClient.printSchedule()
	 */
	@Override
	public void printSchedule() {
		for (Course course : user_schedule) {
			System.out.println(course.toString());
		}
	}

	/**
	 * See SecureClient.run()
	 */
	@Override
	public void run() {
		getOptions();

		int option = getInput();

		while (option != Client.EXIT) {
			if (option > max_option) {
				System.err.println("Insufficient privileges to perform action!");
			} else {
				switch (option) {
				case Client.HELP:
					getOptions();
					break;
				case Client.ADD_COURSE:
					System.out.println("Adding course...");
					addCourse();
					break;
				case Client.DROP_COURSE:
					System.out.println("Dropping course...");
					dropCourse();
					break;
				case Client.SEARCH_COURSE:
					System.out.println("Searching for a course...");
					searchCourse();
					break;
				case Client.PRINT_SCHEDULE:
					System.out.println("Print schedule...");
					printSchedule();
					break;
				case -1:
					System.err.println("Error getting option in +AdminClient.run(): void");
					Main.user_input.close();
					System.exit(Client.FATAL);
					break;
				default:
					System.err.println("Uknown option (" + option + ") in +AdminClient.run(): void");
					break;
				}
			}

			option = getInput();
		}

		exitWithCode(Client.SUCCESS);
	}

	/**
	 * See SecureClient.exitWithCode(int)
	 */
	@Override
	public void exitWithCode(int error_code) {
		if (error_code == Client.FATAL_UNDEFINED) {
			System.err.println("Some method was implemented but never defined properly");
		}
		this.return_code = error_code;
	}
}