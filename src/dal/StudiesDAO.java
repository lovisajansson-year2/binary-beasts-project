package dal;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

import database.DatabaseConnection;
import javafx.collections.*;
import models.Course;
import models.Student;

public class StudiesDAO {

	public static void addStudies(int studentID, int courseCode) throws SQLException, ClassNotFoundException{
		  String stmt =
	                "insert into Studies values("+studentID+", "+courseCode+")";
	        try {
	            DatabaseConnection.dbExecuteUpdate(0, stmt);
	        } catch (SQLException e) {
	            System.out.println("Error while inserting studies");
	            throw e;
	        }
	}
	public static void removeStudies(int studentID, int courseCode) throws SQLException, ClassNotFoundException{
		String stmt = "delete from Studies where studentID = "+studentID+" and courseCode="+courseCode+"";
        try {
            DatabaseConnection.dbExecuteUpdate(0, stmt);
        } catch (SQLException e) {
            System.out.println("Error while deleting studies.");
            throw e;
        }
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
	
	public static String getAllUnfinishedCourseStmt() {
		String stmt = "select studentID from Studies where courseCode="; 
		return stmt;
	}

	public static ObservableList<Course> findAllStudiesForStudents(int studentID) throws SQLException, ClassNotFoundException{
		String stmt = "select c.courseCode, c.credits from course c join studies s on c.courseCode = s.courseCode where studentID = "+studentID+"";
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

	public static String getStartedStmt(int cID) {
		String stmt = "select * from Studies where courseCode="+cID;
		return stmt;
	}

	public static String getAllStudies() {
		return "select * from studies";
	}
}

