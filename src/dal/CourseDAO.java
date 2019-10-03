package dal;

import database.DatabaseConnection;
import javafx.collections.*;
import models.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

public class CourseDAO {

	public static int addCourse(int credits) throws SQLException, ClassNotFoundException {
		String stmt =
				"insert into course values('"+credits+"');";
		int id = 0;
		String stmt2 = "select max(courseCode) from course";

		try {
			DatabaseConnection.dbExecuteUpdate(0, stmt);
			ResultSet rs = DatabaseConnection.dbExecuteQuery(0,stmt2);
			while (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println("Error while inserting Course");
			throw e;
		}
		return id;
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

		String stmt = "select * from course where courseCode = '" +courseCode+"'";

		try {
			ResultSet rs = DatabaseConnection.dbExecuteQuery(0, stmt);

			Course course = getCourse(rs);

			return course;

		} catch (SQLException e) {
			System.out.println("Error while running findCourse in dao");
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
	private static Course getCourse(ResultSet rs) throws SQLException {
		Course tmp = new Course();

		if(rs.next()) {
			tmp.setCourseCode(rs.getInt("courseCode"));
			tmp.setCredits(rs.getInt("credits"));
		}
	return tmp;

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
	public static ResultSet getAllCourses() throws SQLException, ClassNotFoundException {
		String stmt = "select * from course";
		ResultSet rs = DatabaseConnection.dbExecuteQuery(0, stmt);
		return rs;
	}
	public static ResultSet getSpecificCourse(int cID) throws SQLException, ClassNotFoundException {
		String stmt =  "select * from course where courseCode ="+cID ;
		ResultSet rs = DatabaseConnection.dbExecuteQuery(0, stmt);
		return rs;
	}
	
	public static ResultSet getCoursesDetailed() throws SQLException, ClassNotFoundException {
		String stmt = "select c.courseCode, c.credits,\n" +
				"(select count(*) from hasStudied hs where hs.courseCode = c.courseCode and grade = 'A') * 100 / \n" +
				"(select count(*) from hasStudied hs1 where hs1.courseCode = c.courseCode) as '% of A''s',\n" +
				"(select count(*) from hasStudied hs where hs.courseCode = c.courseCode and grade != 'F') * 100 /\n" +
				"(select count(*) from hasStudied hs1 where hs1.courseCode = c.courseCode) as 'Troughput'\n" +
				"from course c\n" +
				"join hasStudied hs3 on c.courseCode = hs3.courseCode\n" +
				"group by c.courseCode, c.credits";
		ResultSet rs = DatabaseConnection.dbExecuteQuery(0, stmt);
		return rs;
	}
	
	public static ResultSet getSpecificCourseDetailed(int cID) throws SQLException, ClassNotFoundException {
		String stmt = "select c.courseCode, c.credits," +
				"(select count(*) from hasStudied hs where hs.courseCode = c.courseCode and grade = 'A') * 100 / "+
			    "(select count(*) from hasStudied hs1 where hs1.courseCode = c.courseCode) as '% of A''s',"+
				"(select count(*) from hasStudied hs where hs.courseCode = c.courseCode and grade != 'F') * 100 /" +
			    "(select count(*) from hasStudied hs1 where hs1.courseCode = c.courseCode) as 'Troughput'\n"+
			    "from course c\n"+
			    "join hasStudied hs3 on c.courseCode = hs3.courseCode\n"+
			    "where c.courseCode="+cID+" \n"+
			    "group by c.courseCode, c.credits";
		ResultSet rs = DatabaseConnection.dbExecuteQuery(0, stmt);
		return rs;
	}
	
	


	
}
