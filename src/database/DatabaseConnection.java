package database;

import com.sun.javafx.scene.control.Properties;

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
		 
	private static final String connStr = "jdbc:sqlserver://127.0.0.1:1433;connectTimeout=10;loginTimeout=10;database=Assignment1;";
	private static final String connStr1 = "jdbc:sqlserver://127.0.0.1:1433;connectTimeout=10;loginTimeout=10;database=CRONUS;";

	private static final String userName = "user";
	private static final String password = "pass";


	public static void dbConnect(int index) throws SQLException, ClassNotFoundException {
		    	String message = null;
		        try {
		            Class.forName(JDBC_DRIVER);
		            System.out.println("Driver established.");
		        } catch (ClassNotFoundException e) {
		           	message = "No JBDC driver found.";
		           	System.out.println(message);
		            e.printStackTrace();
		            throw e;
		        }
		        
		        try {
		        	if(index == 0) {
						conn = DriverManager.getConnection(connStr, userName, password);
					} else if(index == 1) {
						conn = DriverManager.getConnection(connStr1, userName, password);
					}
		        } catch (SQLException e) {
		            System.out.println("Connection Failed! Check output console" + e);
		            e.printStackTrace();
		          //  throw e;
		        }
		    }
	public static void dbDisconnect() throws SQLException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e){
           throw e;
        }
    }
		 
		 public static ResultSet dbExecuteQuery(int index, String queryStmt) throws SQLException, ClassNotFoundException {
		        Statement stmt = null;
		        ResultSet resultSet = null;
		        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
		        try {
		            dbConnect(index);
		 
		            stmt = conn.createStatement();
		 			stmt.setQueryTimeout(10);
		            resultSet = stmt.executeQuery(queryStmt);
		
		            crs.populate(resultSet);
		        } catch (SQLException e) {
		            System.out.println("Problem occurred at executeQuery operation : " + e);
		            throw e;
		        } finally {
		            if (resultSet != null) {
		                resultSet.close();
		            }
		            if (stmt != null) {
		             
		                stmt.close();
		            }
		            dbDisconnect();
		        }
		        return crs;
		    }
		 
	 public static void dbExecuteUpdate(int index, String sqlStmt) throws SQLException, ClassNotFoundException {
		        Statement stmt = null;
		        try {
		            dbConnect(index);
		            stmt = conn.createStatement();
		            stmt.setQueryTimeout(10);
		            stmt.executeUpdate(sqlStmt);
		        } catch (SQLException e) {
		            System.out.println("Problem occurred at executeUpdate operation : " + e);
		            throw e;
		        }finally {
		        	if (stmt != null) {
		                stmt.close();
		        	}
		            dbDisconnect();
	}

}
}	 

