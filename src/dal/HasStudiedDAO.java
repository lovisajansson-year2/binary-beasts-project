package dal;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Course;
import models.Student;

public class HasStudiedDAO {

	public static void addHasStudied (int studentID, int courseCode, String grade) throws SQLException,ClassNotFoundException{
		String stmt =
                "insert into HasStudied values("+studentID+","+courseCode+",'"+grade+"')";
        try {
            DatabaseConnection.dbExecuteUpdate(0, stmt);
        } catch (SQLException e) {
            System.out.println("Error while inserting studies");
            throw e;
        }
	}
	
	public static String findGrade(int studentID, int courseCode) throws SQLException, ClassNotFoundException{
		String stmt = "select grade from HasStudied where studentID = "+studentID+" and courseCode="+courseCode+"";
        String grade = null;
		try {
            ResultSet rs = DatabaseConnection.dbExecuteQuery(0, stmt);
            while (rs.next()) {
            	grade = rs.getString(1);
            }
        } catch (SQLException e) {
            System.out.println("Error while finding grade.");
            throw e;
        }
        return grade;
	}

	private static ObservableList<Course> getCourseList(CachedRowSet crs) throws SQLException, ClassNotFoundException{
		ObservableList<Course> cList = FXCollections.observableArrayList();
        while (crs.next()) {
        	Course c= new Course();
        	c.setCourseCode(crs.getInt(1));
        	cList.add(c);
        }
        return cList;
	}

	public static ObservableList<Course> findAllCompletedCourses(int studentID) throws SQLException, ClassNotFoundException{
		String stmt = "select courseCode from HasStudied where studentID ="+studentID+"";
		ResultSet rs = null;
		CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
		try {
			rs = DatabaseConnection.dbExecuteQuery(0, stmt);
			crs.populate(rs);
			ObservableList<Course> cList = getCourseList(crs);
			return cList;
		} catch (SQLException e) {
			System.out.println("error while finding completed courses");
			throw e;
		}
	}
	
	public static ResultSet getAllCompletedCourseStmt() throws SQLException, ClassNotFoundException {
		String stmt = "select * from HasStudied where courseCode=";
		ResultSet rs = DatabaseConnection.dbExecuteQuery(0, stmt);
		return rs;
	}
	
	public static ResultSet getCompletedStmt(int cID) throws SQLException, ClassNotFoundException {
		String stmt = "select * from HasStudied where courseCode="+cID;
		ResultSet rs = DatabaseConnection.dbExecuteQuery(0, stmt);
		return rs;
	}
	public static ResultSet getAllHasStudied() throws SQLException, ClassNotFoundException {
		String stmt = "select * from hasStudied";
		ResultSet rs = DatabaseConnection.dbExecuteQuery(0, stmt);
		return rs;
	}
}