package database;

import java.sql.*;
public class Test {

	public static void main(String[] args) throws SQLException, ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		DatabaseConnection db = new DatabaseConnection();
		ResultSet rs = db.dbExecuteQuery("select * from Course");
		while (rs.next()) {
			System.out.println(rs.getString(1));
		}
	}

}
