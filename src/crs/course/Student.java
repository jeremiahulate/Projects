package crs.course;

import java.util.List;

import crs.Course;
import crs.SecurityLevel;

public class Student {
	
	private String student_name;
	private List<Course> previously_enrolled;
	private List<Course> currently_enrolled;

	protected int student_ID;
	protected int hoursEnrolled;
	
	private SecurityLevel security_level;
	
	public Student(SecurityLevel security_level, int student_ID, String student_name) {
		this.student_ID = student_ID;
		this.student_name = student_name;
		this.security_level = security_level;
	}
	
	public String getStudentName() {
		return student_name;
	}
	
	public List<Course> getPreviouslyEnrolled() {
		return previously_enrolled;
	}
	
	public List<Course> getCurrentlyEnrolled() {
		return currently_enrolled;
	}
	
	public void enrollCourse(Course course) {
		
	}
	
	public void dropCourse(String fullCourseName) {
		
	}
	
	public SecurityLevel getSecurityLevel() {
		return this.security_level;
	}
}