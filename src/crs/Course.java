package crs;

import java.util.ArrayList;

/**
 * An abstract representation of a course. There are two types of courses
 * - Lectures, which may have a linked lab
 * - Labs, which have a linked lecture
 * 
 * @author Christopher Harris
 * @version 1.0
 */
public abstract class Course {

	// The department code of the course, e.g. PHYS, CHEM, CS, etc...
	public String department;

	// The full name of the course
	public String course_name;

	// The description of the course
	public String description;

	// The name of the course instructor
	public String instructor;

	// The number of the course code, e.g. 1301, 3361, etc...
	public int course_number;

	// The number of credit hours the course is worth
	public int credit_hours;

	// The course registration number of the linked course. -1 if there is none
	public int linked_crn;
	
	// The number of seats available in the class
	public int seats;
	
	// The course registration number of the course. 5 digits for a lab, 6 for a lecture
	public int crn;
	
	// The names of all the TAs for the course
	public ArrayList<String> TAs = new ArrayList<>();

	/**
	 * Creates a unique string representation for the course.
	 * Different between labs and lectures
	 * @return A string representation of the course
	 */
	public abstract String stringRepresentation();

	/**
	 * Creates a unique string representation for the course.
	 * Different between labs and lectures
	 * 
	 * Compared to stringRepresentation(), minimalStringRepresentation() returns a 
	 * string that contains the minimum amount of information
	 * @return A minimial string representation of the course
	 */
	public abstract String minimalStringRepresentation();

	@Override
	public String toString() {
		return stringRepresentation();
	}

}