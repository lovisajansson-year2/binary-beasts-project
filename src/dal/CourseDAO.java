package dal;
import database.DatabaseConnection;
import models.*;
import dal.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

public class CourseDAO {
	private static int count = 100; //fixa
	
	public static String generateID() {
		String cID = "C" + count++;
		return cID;
	}
	public static void addCourse(Integer credits) throws SQLException, ClassNotFoundException {
		String stmt = "insert into Course values('"+generateID()+"','"+credits+"')";
	
		try {
			DatabaseConnection.dbExecuteUpdate(stmt);
		} catch (SQLException e) {
			System.out.println("Error while inserting Course");
			throw e;
		}
	}
	public static ResultSet findCourse(String courseCode) throws SQLException, ClassNotFoundException{
		String stmt = "select * from Course where courseCode ='"+courseCode+"'"; 
		ResultSet rs = null;
		CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
		try {
			rs = DatabaseConnection.dbExecuteQuery(stmt);
			crs.populate(rs);
		} catch (SQLException e) {
			System.out.println("error while finding student");
		}
		return crs;
	}
	
}
