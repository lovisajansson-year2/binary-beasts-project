package test;

import database.DatabaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Test {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        DatabaseConnection db = new DatabaseConnection();

        ResultSet rs = db.dbExecuteQuery("select * from student");

        while(rs.next()) {
            System.out.println(rs.getString(1));
       }
    }

}
