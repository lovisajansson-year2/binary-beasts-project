package dal;

import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseConnection;
import models.Course;

public class CourseDAO {
	public static Course findCourse (String courseCode) throws SQLException, ClassNotFoundException {
		String selectStmt = "SELECT * FROM Course WHERE courseCode ="+courseCode;
		
		try {
			ResultSet rsCourse = DatabaseConnection.dbExecuteQuery(selectStmt);
			Course course = getCourseFromResultSet(rsCourse);
			
			return course;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public static Course getCourseFromResultSet(ResultSet rs) throws SQLException {
		Course course = null;
		if (rs.next()) {
			course = new Course();
			course.setCourseCode(rs.getString("courseCode"));
			course.setCourseName(rs.getString("courseName"));
			course.setCredits(rs.getInt("credits"));
		}
		return course;
	}
	
	/*public Course registerCourse(ResultSet rs) throws SQLException {
		Course course = new Course();
		try {
			course.setCourseCode(rs.getString("courseCode"));
			course.setCourseName(rs.getString("courseName"));
			course.setCredits(rs.getInt("credits"));
		} catch (SQLException e) {
			throw e;
		}
		return course;
	}*/
	
	
	public static void updateCourse (String courseCode, String courseName) throws SQLException{
		
	}
}
