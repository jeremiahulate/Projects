package crs.course;

import crs.Course;
import crs.Main;

/**
 * @author Christopher Harris
 * @author Jason Tran
 * @author Jeremiah Ulate
 * @version 1.0
 */
public class Lab extends Course {

	/**
	 * See Course.stringRepresentation()
	 */
	@Override
	public String stringRepresentation() {
		return "\n\t\t(Lab)" + this.department + " " + this.course_number + " | "
				+ Main.course_registry.searchCourse(linked_crn).course_name + "\n\t\tInstructor: " + this.instructor
				+ "\n\t\tTAs: " + this.TAs.toString() + "\n\t\tHours: Lab" + "\n\t\tSeats Remaining: "
				+ this.seats + "\n\t\tDescription: "
				+ Main.course_registry.searchCourse(linked_crn).description;
	}

	/**
	 * See Course.minimalStringRepresentation()
	 */
	@Override
	public String minimalStringRepresentation() {
		return "(Lab): " + this.department + " " + this.course_number + " | " + this.crn + " with"
				+ this.TAs.toString().replace('[', ' ').replace(']', ' ') + "<Linked Lecture: " + this.linked_crn + ">";
	}
}