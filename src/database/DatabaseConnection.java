package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

public class DatabaseConnection {
	
	private static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	private static Connection conn = null;
		 
	private static final String connStr = "jdbc:sqlserver://127.0.0.1:1433;database=Assignment1;";

	private static final String userName = "user";
	
	private static final String password = "pass";

	public static void dbConnect() throws SQLException, ClassNotFoundException {
		    	String message = null;
		        try {
		            Class.forName(JDBC_DRIVER);
		        } catch (ClassNotFoundException e) {
		           	message = "No JBDC driver found.";
		           	System.out.println(message);
		            e.printStackTrace();
		            throw e;
		        }

		        message = "JDBC Driver Registered!";
	           	System.out.println(message);

		        try {
		            conn = DriverManager.getConnection(connStr, userName, password);
		            System.out.println("Connection succeeded!");
		        } catch (SQLException e) {
		            System.out.println("Connection Failed! Check output console" + e);
		            e.printStackTrace();
		            throw e;
		        }
		    }
		 
		 public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {
		        Statement stmt = null;
		        ResultSet resultSet = null;
		        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
		        try {
		            dbConnect();
		            System.out.println("Statement: " + queryStmt + "\n");
		 
		            stmt = conn.createStatement();
		 
		            resultSet = stmt.executeQuery(queryStmt);
		
		            crs.populate(resultSet);
		        } catch (SQLException e) {
		            System.out.println("Problem occurred at executeQuery operation : " + e);
		            throw e;
		        } 
		        return crs;
		    }
		 
	 public static void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
		        Statement stmt = null;
		        try {
		            dbConnect();
		            stmt = conn.createStatement();
		            stmt.executeUpdate(sqlStmt);
		        } catch (SQLException e) {
		            System.out.println("Problem occurred at executeUpdate operation : " + e);
		            throw e;
		        }
	}

}

