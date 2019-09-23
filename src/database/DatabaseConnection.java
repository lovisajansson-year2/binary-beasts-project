package database;

import java.sql.*;


public class DatabaseConnection {

    public static void main(String[] args) {

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch(ClassNotFoundException e    ) {
            e.printStackTrace();
        }

        String url = "jdbc:sqlserver://127.0.0.1:1433;database=Assignment1;";
        String user = "user";
        String pass = "pass";

        try {
            Connection con = DriverManager.getConnection(url, user, pass);
            String query = "SELECT * FROM Student";
            PreparedStatement ps  = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

}
