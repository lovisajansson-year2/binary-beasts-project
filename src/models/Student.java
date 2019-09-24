package models;

public class Student {

	private String studentID;
	private String firstName;
	private String lastName;
	private Grade grade;

	public Student(String studentID, String firstName, String lastName) {
		this.studentID = studentID;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getStudentID() {
		return studentID;
	}
	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}
	public Grade getGrade() {
		return grade;
	}
	public void setGrade(Grade grade) {
		this.grade = grade;
	}
}
