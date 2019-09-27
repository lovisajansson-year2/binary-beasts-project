package test;

import java.sql.ResultSet;
import java.sql.SQLException;
import database.DatabaseConnection;

public class Test {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        DatabaseConnection db = new DatabaseConnection();

        ResultSet rs = db.dbExecuteQuery(0,"select * from student");

        while(rs.next()) {
            System.out.println(rs.getString(1));
       }
    }

}
