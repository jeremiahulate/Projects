package crs.course;

import crs.Course;
import crs.Main;

/**
 * @author Christopher Harris
 * @author Jason Tran
 * @author Jeremiah Ulate
 * @version 1.0
 */
public class Lecture extends Course {

	/**
	 * See Course.stringRepresentation()
	 */
	@Override
	public String stringRepresentation() {
		if (this.linked_crn > 0) {
			return this.department + " " + this.course_number + " | " + this.course_name + "\n\tInstructor: " + this.instructor
					+ "\n\tTAs: " + this.TAs.toString() + "\n\tHours: " + this.credit_hours + "\n\tSeats Remaining: "
					+ this.seats + "\n\tLinked Lab: "
					+ Main.course_registry.searchCourse(linked_crn).toString() + "\n\tDescription: " + this.description;
		} else {
			return this.department + " " + this.course_number + " | " + this.course_name + "\n\tInstructor: " + this.instructor
					+ "\n\tTAs: " + this.TAs.toString() + "\n\tHours: " + this.credit_hours + "\n\tSeats Remaining: "
					+ this.seats + "\n\tDescription: " + this.description;
		}
	}

	/**
	 * See Course.minimalStringRepresentation()
	 */
	@Override
	public String minimalStringRepresentation() {
		if (this.linked_crn > 0) {
			return this.department + " " + this.course_number + " | " + this.crn + " " + this.course_name + " with "
					+ this.instructor + " <Linked Lab: " + this.linked_crn + ">";
		} else {
			return this.department + " " + this.course_number + " | " + this.crn + " " + this.course_name + " with "
					+ this.instructor;
		}
	}
}