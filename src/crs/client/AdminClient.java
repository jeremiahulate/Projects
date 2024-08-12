package crs.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import crs.CRSArgumentsHandler;
import crs.Client;
import crs.Course;
import crs.Main;
import crs.SecurityLevel;
import crs.client.filehandling.FileHandler;
import crs.course.Lab;
import crs.course.Lecture;

/**
 * An instance of a SecureClient that has admin privileges.
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
public class AdminClient extends SecureClient {

	/**
	 * The constructor is private because an AdminClient should only
	 * be instantiated using the loadClient() method
	 * @param user_key The key provided by the user
	 * @param base_level The base security level before any elevation change
	 */
	private AdminClient(String user_key, SecurityLevel base_level) {
		super(user_key, base_level);
	}

	/**
	 * See SecureClient.calculateElevationChange
	 */
	@Override
	protected SecurityLevel calculateElevationChange(SecurityLevel base_level, String user_key) {
		if (base_level != SecurityLevel.LEVEL_NONE && base_level != SecurityLevel.LEVEL_STUDENT) {
			return SecurityLevel.LEVEL_ADMIN;
		} else {
			return base_level;
		}
	}

	/**
	 * See SecureClient.getOptions()
	 */
	@Override
	public void getOptions() {
		super.getOptions();
	}
	
	/**
	 * See SecureClient.addCourse()
	 */
	@Override
	public void addCourse() {
		System.out.print("Enter CRN: ");
		int lec_crn = Main.user_input.nextInt();
		if (Integer.toString(lec_crn).length() != 6) {
			if (Integer.toString(lec_crn).length() == 5) {
				System.out
						.println("Adding linked labs directly is not supported. Add the appropriate lecture instead!");
				return;
			} else {
				System.out.println("CRN must be of length 6!");
				return;
			}
		}
		Course lec = Main.course_registry.searchCourse(lec_crn);
		if (lec != null) {
			user_schedule.add(lec);
			lec.seats--;
			if (lec.linked_crn > 0) {
				// There is a linked lab, so we need to decrement seats there too
				Course lab = Main.course_registry.searchCourse(lec.linked_crn);
				if (lab != null)
					lab.seats--;
				else {
					System.out.println("No linked lab found matching desired CRN " + lec.linked_crn
							+ ". This indicates an error in the registry data file");
				}
			}
		} else {
			System.out.println("No course found matching CRN " + lec_crn);
		}
	}

	/**
	 * See SecureClient.dropCourse()
	 */
	@Override
	public void dropCourse() {
		System.out.print("Enter CRN: ");
		int lec_crn = Main.user_input.nextInt();
		if (Integer.toString(lec_crn).length() != 6) {
			if (Integer.toString(lec_crn).length() == 5) {
				System.out
						.println("Adding linked labs directly is not supported. Add the appropriate lecture instead!");
				return;
			} else {
				System.out.println("CRN must be of length 6!");
				return;
			}
		}
		Course lec = Main.course_registry.searchCourse(lec_crn);
		if (lec != null) {
			boolean in_schedule = user_schedule.remove(lec);
			if (!in_schedule) {
				System.out.println("Error: Course CRN " + lec_crn + " not in your schedule!");
				return;
			}
			lec.seats++;
			if (lec.linked_crn > 0) {
				// There is a linked lab, so we need to decrement seats there too
				Course lab = Main.course_registry.searchCourse(lec.linked_crn);
				if (lab != null)
					lab.seats++;
				else {
					System.out.println("No linked lab found matching desired CRN " + lec.linked_crn
							+ ". This indicates an error in the registry data file");
				}
			}
		} else {
			System.out.println("No course found matching CRN " + lec_crn);
		}
	}

	/**
	 * See SecureClient.searchCourse()
	 */
	@Override
	public void searchCourse() {
		System.out.print("By CRN(0) or by Name(1): ");
		int opt = Main.user_input.nextInt();
		if (opt == 0) {
			System.out.print("Enter CRN: ");
			int crn = Main.user_input.nextInt();
			Course c = Main.course_registry.searchCourse(crn);
			if (c != null)
				System.out.println("Found course\n" + c.stringRepresentation());
			else
				System.out.println("No course found matching CRN " + crn);
		} else if (opt == 1) {
			System.out.print("Enter Query (1 word): ");
			String search_query = Main.user_input.next();
			ArrayList<Course> courses = Main.course_registry.searchCourseByName(search_query);
			if (courses != null) {
				System.out.println("Courses found include: ");
				for (Course c : courses) {
					System.out.println("\t" + c.minimalStringRepresentation());
				}
			} else
				System.out.println("No courses found matching query " + search_query);
		} else {
			System.out.println("Invalid option (" + opt + ")");
		}
	}

	/**
	 * See SecureClient.printSchedule()
	 */
	@Override
	public void printSchedule() {
		if (user_schedule.size() == 0) {
			System.out.println("No courses in schedule. Try adding courses using option 3");
			return;
		}

		for (Course course : user_schedule) {
			System.out.println(course.toString());
		}
	}

	/**
	 * Prints the user's current security level
	 */
	public void checkSecurity() {
		System.out.println("Current security level is " + security_level.toString());
	}

	/**
	 * Elevates the user to a sysadmin
	 */
	public void elevate() {
		this.security_level = SecurityLevel.LEVEL_SYSADMIN;
		checkSecurity();
	}

	/**
	 * Links a lab and a lecture together
	 */
	public void linkLabAndLecture() {
		System.out.print("Enter Lecture CRN: ");
		int lec_crn = Main.user_input.nextInt();
		Lecture lec = (Lecture) (Main.course_registry.searchCourse(lec_crn));
		if (lec != null) {
			System.out.print("\nEnter Lab CRN: ");
			int lab_crn = Main.user_input.nextInt();
			Lab lab = (Lab) (Main.course_registry.searchCourse(lab_crn));
			if (lab != null) {
				lec.linked_crn = lab_crn;
				lab.linked_crn = lec_crn;
			} else
				System.out.println("No lab found matching CRN " + lab_crn);
		} else
			System.out.println("No lecture found matching CRN " + lec_crn);
	}

	/**
	 * Loads the prerequisites for each class
	 * @return A HashMap of all the prerequisites for each course
	 */
	private HashMap<String, List<Course>> loadPreReq() {
		return FileHandler.getPrereqs();
	}

	/**
	 * Loads an AdminClient based on the arguments provided by the main method
	 * @param args The arguments provided by the main method
	 * @return A new instance of AdminClient
	 */
	public static AdminClient loadClient(String args[]) {
		String user_key = CRSArgumentsHandler.getParameter("k", "-key", 0);
		SecurityLevel base_level = SecurityLevel.LEVEL_ADMIN;

		AdminClient client = new AdminClient(user_key, base_level);

		client.arguments_handler = new ArgumentsHandler(client.security_level);

		client.prerequisites = client.loadPreReq();

		System.out.println("Welcome, Admin");

		return client;
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
				case Client.CHECK_SECURITY:
					System.out.println("Current security level: " + security_level);
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
				case LINK_LAB_LECTURE:
					System.out.println("Linking lab and lecture...");
					linkLabAndLecture();
					break;
				case PRINT_SCHEDULE:
					System.out.println("Printing schedule...");
					printSchedule();
					break;
				case ELEVATE:
					System.out.println("Elevating...");
					elevate();
					break;
				case -1:
					System.err.println("Error getting option in +AdminClient.run(): void");
					Main.user_input.close();
					System.exit(Client.FATAL);
					break;
				default:
					System.err.println("Unknown option (" + option + ") in +AdminClient.run(): void");
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