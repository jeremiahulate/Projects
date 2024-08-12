package crs.course;

import java.util.ArrayList;

import crs.Course;
import crs.client.filehandling.FileHandler;

/**
 * Contains an ArrayList of courses which represents the course registry
 * and contains methods for loading the registry and searching the registry
 * 
 * @author Christopher Harris
 * @author Jason Tran
 * @author Jeremiah Ulate
 * @version 1.0
 */
public class CourseRegistry {

	// The course registry. Private because other classes cannot
	// modify the registry, only query it
	private ArrayList<Course> courses = new ArrayList<Course>();

	/**
	 * Private constructor because CourseRegistry should never be explicitly
	 * declared.
	 * @param registry_file
	 */
	private CourseRegistry(String registry_file) {
		FileHandler.addFile("registry", registry_file);
		courses = FileHandler.loadRegistry();
	}

	/**
	 * returns the Course matching the specified crn
	 * @param searched_crn The crn to search for
	 * @return The Course matching the specified crn. null if there is no matching crn
	 */
	public Course searchCourse(int searched_crn) {
		for (int i = 0; i < courses.size(); i++) {
			if (courses.get(i).crn == searched_crn) {
				return courses.get(i);
			}
		}

		return null;
	}

	/**
	 * returns the Course where the course info contains the search query
	 * @param search_query The query to search for
	 * @return The Course where the course info contains the search query. 
	 * 		    null if there is no course matching the query
	 */
	public ArrayList<Course> searchCourseByName(String search_query) {
		ArrayList<Course> result = new ArrayList<Course>();

		for (int i = 0; i < courses.size(); i++) {
			if (courses.get(i).stringRepresentation().toLowerCase().trim().contains(search_query.toLowerCase().trim())) {
				result.add(courses.get(i));
			} else if (courses.get(i).description != null) {
				if (courses.get(i).description.toLowerCase().trim().contains(search_query.toLowerCase().trim())) {
					result.add(courses.get(i));
				}
			} else if (courses.get(i).course_name != null) {
				if (courses.get(i).course_name.toLowerCase().trim().contains(search_query.toLowerCase().trim())) {
					result.add(courses.get(i));
				}
			} else if (courses.get(i).instructor != null) {
				if (courses.get(i).instructor.toLowerCase().trim().contains(search_query.toLowerCase().trim())) {
					result.add(courses.get(i));
				}
			} else if (courses.get(i).TAs != null) {
				if (courses.get(i).TAs.toString().toLowerCase().trim().contains(search_query.toLowerCase().trim())) {
					result.add(courses.get(i));
				}
			}
		}

		if (result.size() == 0)
			return null;
		else
			return result;
	}

	/**
	 * Loads the course registry from a specified filepath
	 * @param registry_file The filepath of the registry file
	 * @return A new CourseRegistry object
	 */
	public static CourseRegistry loadRegistryFromFile(String registry_file) {
		return new CourseRegistry(registry_file);
	}
}