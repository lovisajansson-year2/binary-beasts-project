package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class testDBConnection {
	public static void main(String args[]) throws SQLException, ClassNotFoundException {	    

	    DatabaseConnection.dbExecuteQuery("SELECT * FROM Student");


	}
}
