package database;

import java.sql.*;

import javax.sql.rowset.CachedRowSet;

import dal.CourseDAO;
public class Test {

	public static void main(String[] args) throws SQLException, ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		DatabaseConnection db = new DatabaseConnection();
		CachedRowSet crs = CourseDAO.findCourse("c2");
		while (crs.next()) {
			System.out.println(crs.getString(1));
			System.out.println(crs.getFloat(2));
		}
	}

}
