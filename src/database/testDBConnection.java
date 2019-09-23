package database;

import java.sql.SQLException;

public class testDBConnection {
	public static void main(String args[]) throws SQLException, ClassNotFoundException {	    

	    DatabaseConnection.dbExecuteQuery("SELECT * FROM Student");

	}
}
