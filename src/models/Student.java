package models;

public class Student {

	private int studentID;
	private String firstName;
	private String lastName;
	private HasStudied grade;

	public Student() {
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
	public int getStudentID() {
		return studentID;
	}
	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}
	public HasStudied getGrade() {
		return grade;
	}
	public void setGrade(HasStudied grade) {
		this.grade = grade;
	}

	

}
