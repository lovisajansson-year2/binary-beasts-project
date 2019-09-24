package dal;

import database.DatabaseConnection;
import models.*;
import dal.*;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;


public class StudentDAO {
    private static int count = 100;

    public static String generateID() {
        String sID = "S" +  count++;
        return sID;
    }

    public static void addStudent(String fName, String lName) throws SQLException, ClassNotFoundException{

        String stmt =
                "insert into Student VALUES('"+generateID()+"', '"+lName+"', '"+fName+"');";

        try {
            DatabaseConnection.dbExecuteUpdate(stmt);
        } catch (SQLException e) {
            System.out.println("Error while inserting student");
            throw e;
        }

    }
}
