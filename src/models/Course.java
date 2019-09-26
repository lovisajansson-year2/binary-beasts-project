package models;

public class Course {
	private int courseCode;
	private String courseName;
	private int credits;
	private HasStudied grade;

	public Course() {
	}

	public Course(int courseCode, String courseName, int credits) {
		this.courseCode = courseCode;
		this.courseName = courseName;
		this.credits = credits;
	}
	
	public int getCourseCode() {
		return courseCode;
	}
	public void setCourseCode(int i) {
		this.courseCode = i;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public int getCredits() {
		return credits;
	}
	public void setCredits(int credits) {
		this.credits = credits;
	}

	public HasStudied getGrade() {
		return grade;
	}

	public void setGrade(HasStudied grade) {
		this.grade = grade;
	}
}
