package dal;

import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.*;
import dal.*;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentDAO {

    public static int generateID() throws SQLException, ClassNotFoundException {
        int id = 0;

        String stmt = "select max(studentID) from student";

        try {
            ResultSet rs = DatabaseConnection.dbExecuteQuery(stmt);
            if(rs.next()) {
                id = rs.getInt(1);
                id++;
            }

        } catch(SQLException e) {
            System.out.println("Error generating id");
            throw e;
        }
        return id;

    }

    public static Student findStudent(String studentID) throws SQLException, ClassNotFoundException {

        String stmt = "select * from student where studentID = '" +studentID+"'";

        try {
            ResultSet rs = DatabaseConnection.dbExecuteQuery(stmt);

            Student student = getStudent(rs);

            return student;

        } catch (SQLException e) {
            System.out.println("Error while running findStudent in dao");
            throw e;
        }

    }

    public static Student getStudent(ResultSet rs) throws SQLException, ClassNotFoundException {
        Student tmp = null;

        if (rs.next()) {
            tmp.setStudentID(rs.getString("studentID"));
            tmp.setFirstName(rs.getString("firstName"));
            tmp.setLastName(rs.getString("lastName"));
        }
        return tmp;
    }

    public static ObservableList<Student> findAllStudent() throws SQLException, ClassNotFoundException {
        String stmt = "select * from student";

        try {
            ResultSet rs = DatabaseConnection.dbExecuteQuery(stmt);

            ObservableList<Student> studentList = getAllStudent(rs);

            return studentList;


        } catch (SQLException e) {
            System.out.println("Error while running findAllStudent in dao");
            throw e;
        }

    }

    public static ObservableList<Student> getAllStudent(ResultSet rs) throws SQLException, ClassNotFoundException {
        ObservableList<Student> studentList = FXCollections.observableArrayList();

       while (rs.next()) {
           Student tmp = new Student();
           tmp.setStudentID(rs.getString("studentID"));
           tmp.setFirstName(rs.getString("firstName"));
           tmp.setLastName(rs.getString("lastName"));
           studentList.add(tmp);
       }
       return studentList;

    }


    public static void addStudent(String fName, String lName) throws SQLException, ClassNotFoundException{

        String stmt =
                "insert into Student VALUES('"+generateID()+"', '"+fName+"', '"+lName+"');";

        try {
            DatabaseConnection.dbExecuteUpdate(stmt);
        } catch (SQLException e) {
            System.out.println("Error while inserting student");
            throw e;
        }
    }

    public static void updateStudent(String studentID, String fName, String lName) throws SQLException, ClassNotFoundException {

        String stmt = "update student set firstName = '" +fName+ "', lastName = '" +lName+ "' where studentID = '" +studentID+"'";

        try {
            DatabaseConnection.dbExecuteUpdate(stmt);
        } catch (SQLException e) {
            System.out.println("Error while updating student.");
            throw e;
        }
    }

    public static void removeStudent(String studentID) throws SQLException, ClassNotFoundException {

        String stmt = "delete from student where studentID = '"+studentID+"'";

        try {
            DatabaseConnection.dbExecuteUpdate(stmt);
        } catch (SQLException e) {
            System.out.println("Error while deleting student.");
            throw e;
        }
    }
}
