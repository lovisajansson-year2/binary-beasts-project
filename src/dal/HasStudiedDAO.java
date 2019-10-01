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
	public static void removeHasStudied(int studentID, int courseCode) throws SQLException, ClassNotFoundException{
		String stmt = "delete from HasStudied where studentID = "+studentID+" and courseCode="+courseCode+"";
        try {
            DatabaseConnection.dbExecuteUpdate(0, stmt);
        } catch (SQLException e) {
            System.out.println("Error while deleting studies.");
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
	private static ObservableList<Student> getStudentList(CachedRowSet crs) throws SQLException, ClassNotFoundException{
		ObservableList<Student> sList = FXCollections.observableArrayList();
        while (crs.next()) {
        	Student s= new Student();
        	s.setStudentID(crs.getInt(1));
        	sList.add(s);
        }
        return sList;
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
	public static ObservableList<Student> findAllCompletedStudents(int courseCode) throws SQLException, ClassNotFoundException{
		String stmt = "select studentId from HasStudied where courseCode ="+courseCode+"";
		ResultSet rs = null;
		CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
		try {
			rs = DatabaseConnection.dbExecuteQuery(0, stmt);
			crs.populate(rs);
			ObservableList<Student> sList = getStudentList(crs);
			return sList;
		} catch (SQLException e) {
			System.out.println("error while finding students");
			throw e;
		}
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
	
	public static String getAllCompletedCourseStmt() {
		String stmt = "select * from HasStudied where courseCode=";
		return stmt;
	}
	
	public static String getAllResultsStudent() {
		String stmt = "select courseCode, grade from HasStudied where studentID=";
		return stmt;
	}
	
	public static int nbrOfStudentsWithAGrade(int courseCode) throws ClassNotFoundException {
		String stmt = "select count(grade) from HasStudied where courseCode ="+courseCode+" and grade="+'A'+"";
		ResultSet rs = null;
		int nbrOfStudents= 0;
		try {
			rs = DatabaseConnection.dbExecuteQuery(0, stmt);
			if(rs.next()) {
				nbrOfStudents = Integer.parseInt(rs.getString(1));
			}	
		} catch (SQLException e) {
			System.out.println("error while finding number of students with top grade");
		}
		return nbrOfStudents;
	}
	public static ObservableList<Course> findHighestThroughput() throws SQLException, ClassNotFoundException{
		String stmt = "select courseCode, count(studentID) nbrofStudents from HasStudied where grade in('A','B','C','D','E')\r\n" + 
				"group by CourseCode\r\n" + 
				"order by  nbrofStudents desc";
		ResultSet rs = null;
		CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
		try {
			rs = DatabaseConnection.dbExecuteQuery(0, stmt);
			crs.populate(rs);
			ObservableList<Course> cList = getCourseList(crs);
			return cList;
		} catch (SQLException e) {
		System.out.println("error when finding throughput");
		throw e;
		}
	}
}