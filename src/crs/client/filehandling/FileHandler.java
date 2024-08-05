package crs.client.filehandling;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import crs.Course;
import crs.course.Lab;
import crs.course.Lecture;

/**
 * @author Christopher Harris
 * @author Jason Tran
 * @author Jeremiah Ulate
 */
public class FileHandler {
	
	// The files loaded by the file handler
	private static HashMap<String, ArrayList<String>> files = new HashMap<>();

	/**
	 * Loads a file into the HashMap, 'files', line by line
	 * @param key The key to save the file into
	 * @param file_path The file path to load
	 */
	public static void addFile(String key, String file_path) {
		try {
			System.out.println("Loading file: " + file_path);
			File file = new File(file_path);
			Scanner scanner = new Scanner(file);
			ArrayList<String> data = new ArrayList<>();

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				data.add(line);
			}

			FileHandler.files.put(key, data);
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes a file from the HashMap, 'files'
	 * @param key The key to remove from 'files'
	 */
	public static void removeFile(String key) {
		FileHandler.files.remove(key);
	}

	/**
	 * Gets all the lines of a specific file loaded by the file handler
	 * @param key The key of the file to load
	 * @return an ArrayList of Strings containing all the lines of the file
	 */
	public static ArrayList<String> getFile(String key) {
		return FileHandler.files.get(key);
	}

	/**
	 * Loads the prerequisites from the prereqs file specified by the user
	 * @return The prerequisites hashmap
	 */
	public static HashMap<String, List<Course>> getPrereqs() {
		HashMap<String, List<Course>> prereqs = new HashMap<>();
		ArrayList<String> prereq_data = FileHandler.getFile("prereqs");

		int line_number = 1;

		for (String line : prereq_data) {
			String[] tokens = line.split("\\s+");
			List<Course> courses = new ArrayList<>();

			if (tokens.length < 3) {
				System.err.println("Malformed prereq data at line " + line_number);
			} else if (tokens[2].equals("|")) {
				for (int i = 3; i < tokens.length; i += 3) {
					Lecture lect = new Lecture();

					lect.department = tokens[i];
					lect.course_number = Integer.parseInt(tokens[i + 1]);

					courses.add(lect);

					if (tokens[i + 2].equals(";")) {
						break;
					}

					if (!tokens[i + 2].equals(",")) {
						System.err.println("Malformed prereq data at line " + line_number);
					} else {
						continue;
					}
				}
			} else if (!tokens[2].equals(";")) {
				System.err.println("Malformed prereq data at line " + line_number);
			}

			prereqs.put(tokens[0] + " " + tokens[1], courses);

			line_number++;
		}

		return prereqs;
	}

	/**
	 * Loads the full course registry from the registry file specified by the user
	 * @return The course registry ArrayList
	 */
	public static ArrayList<Course> loadRegistry() {
		ArrayList<Course> registry = new ArrayList<>();
		ArrayList<String> registry_data = FileHandler.getFile("registry");

		int line_number = 1;

		for (String line : registry_data) {
			String[] tokens = line.split("\\s+");
			
			Course course = null;

			if (tokens[2].equals("|")) {

				String dept = tokens[0];
				int course_number = Integer.parseInt(tokens[1]);

				int section = Integer.parseInt(tokens[3]);

				if (section >= 500) {
					// Lab
					course = new Lab();

					int i = 5;

					int crn = Integer.parseInt(tokens[i]);

					i += 2;
					int linked_lecture = Integer.parseInt(tokens[i]);

					i += 2;
					String instructor_name = "";
					for (; !tokens[i].equals("|"); i++) {
						instructor_name += tokens[i] + " ";
					}

					i++;
					ArrayList<String> TAs = new ArrayList<>();
					if (!tokens[i].equals("x")) {
						String TA_names = "";
						for (; !tokens[i].equals("|"); i++) {
							TA_names += tokens[i] + " ";
						}

						String[] TA_names_list = TA_names.split(",");
						for (String s : TA_names_list) {
							TAs.add(s);
						}
					}

					i++;
					int seats = Integer.parseInt(tokens[i]);
					
					course.course_number = course_number;
					course.credit_hours = Integer.parseInt(Integer.toString(course_number).substring(1, 2));
					course.crn = crn;
					course.department = dept;
					course.seats = seats;
					course.TAs = TAs;
					course.instructor = instructor_name;
					course.linked_crn = linked_lecture;
				} else {
					// Lecture
					course = new Lecture();

					int i = 5;
					String name = "";
					for (; !tokens[i].equals("|"); i++) {
						name += tokens[i] + " ";
					}

					i++;
					String desc = "";
					for (; !tokens[i].equals("|"); i++) {
						desc += tokens[i] + " ";
					}
					
					i++;
					int crn = Integer.parseInt(tokens[i]);

					i += 2;
					int linked_lab = -1;
					if (!tokens[i].equals("x")) {
						linked_lab = Integer.parseInt(tokens[i]);
					}

					i += 2;
					String instructor_name = "";
					for (; !tokens[i].equals("|"); i++) {
						instructor_name += tokens[i] + " ";
					}

					i++;
					ArrayList<String> TAs = new ArrayList<>();
					if (!tokens[i].equals("x")) {
						String TA_names = "";
						for (; !tokens[i].equals("|"); i++) {
							TA_names += tokens[i] + " ";
						}

						String[] TA_names_list = TA_names.split(",");
						for (String s : TA_names_list) {
							TAs.add(s);
						}
					}

					i+=2;
					int seats = Integer.parseInt(tokens[i]);
					
					course.course_name = name;
					course.description = desc;
					course.course_number = course_number;
					course.credit_hours = Integer.parseInt(Integer.toString(course_number).substring(1, 2));
					course.crn = crn;
					course.department = dept;
					course.seats = seats;
					course.TAs = TAs;
					course.instructor = instructor_name;
					course.linked_crn = linked_lab;
				}

			} else {
				System.err.println("Malformed prereq data at line " + line_number);
			}
			
			if (course == null) {
				System.err.println("Error trying to add course at line " + line_number);
			} else {
				registry.add(course);
			}

			line_number++;
		}
		
		return registry;
	}
}