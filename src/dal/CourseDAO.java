package dal;

import database.DatabaseConnection;
import javafx.collections.*;
import models.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

public class CourseDAO {
	 public static int generateID() throws SQLException, ClassNotFoundException {
	        int id = 0;
	        String stmt = "select max(courseCode) from course";

	        try {
	            ResultSet rs = DatabaseConnection.dbExecuteQuery(0, stmt);
	            if(rs.next()) {
	                id = rs.getInt(1);
	                id++;
	            }

	        } catch(SQLException e) {
	            System.out.println("Error generating id");
	            throw e;
	        }
	        return id;

	    }

	public static void addCourse(int credits) throws SQLException, ClassNotFoundException {
		String stmt = "insert into Course values("+generateID()+","+credits+")";
	
		try {
			DatabaseConnection.dbExecuteUpdate(0, stmt);
		} catch (SQLException e) {
			System.out.println("Error while inserting Course");
			throw e;
		}
	}
	public static void updateCourse(int courseCode, int credits) throws SQLException, ClassNotFoundException {
		String stmt = "update Course set credits = "+credits+" where courseCode = "+courseCode+"";
	
		try {
			DatabaseConnection.dbExecuteUpdate(0, stmt);
		} catch (SQLException e) {
			System.out.println("Error while inserting Course");
			throw e;
		}
	}
	
	public static Course findCourse(int courseCode) throws SQLException, ClassNotFoundException{
		String stmt = "select * from Course where courseCode = "+courseCode+""; 
		ResultSet rs = null;
		CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
		try {
			rs = DatabaseConnection.dbExecuteQuery(0, stmt);
			crs.populate(rs);
			Course c = getCourseFromResultSet(crs);
			return c;
		} catch (SQLException e) {
			System.out.println("error while finding student");
			throw e;
		}
	}

	public static void removeCourse(int courseCode) throws SQLException, ClassNotFoundException {
		String stmt = "delete from Course where courseCode="+courseCode+""; 
		try {
			DatabaseConnection.dbExecuteUpdate(0, stmt);
		} catch (SQLException e) {
			System.out.println("Error while deleting Course");
			throw e;
		}
	}
	public static ObservableList<Course> findAllCourses() throws SQLException, ClassNotFoundException{
		String stmt = "select * from Course";
		ResultSet rs = null;
		CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
		try {
			rs = DatabaseConnection.dbExecuteQuery(0, stmt);
			crs.populate(rs);
			ObservableList<Course> cList = getCourseList(crs);
			return cList;
		} catch (SQLException e) {
			System.out.println("error while finding student");
			throw e;
		}
	}
	private static Course getCourseFromResultSet(CachedRowSet crs) throws SQLException {
		Course c = new Course();
		if(crs.next()) {
			c= new Course();
			c.setCourseCode(crs.getInt(1));
			c.setCredits(crs.getInt(2));
		}
	return c;
	}
	private static ObservableList<Course> getCourseList(CachedRowSet crs) throws SQLException, ClassNotFoundException{
        ObservableList<Course> cList = FXCollections.observableArrayList();
        while (crs.next()) {
        	Course c = new Course();
        	c.setCourseCode(crs.getInt(1));
        	c.setCredits(crs.getInt(2));
        	cList.add(c);
        }
        return cList;
	}

	public static ObservableList<String> getListCourses() throws SQLException, ClassNotFoundException {
		ObservableList<String> courses = FXCollections.observableArrayList();
		courses.add("Register course");
		for(Course c : findAllCourses()) {
			courses.add(Integer.toString(c.getCourseCode()));
		}
		return courses;
	}

	
}
