package dal;

import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.*;
import dal.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

public class StudentDAO {

    public static int generateID() throws SQLException, ClassNotFoundException {
        int id = 0;

        String stmt = "select max(studentID) from student";

        try {
            ResultSet rs = DatabaseConnection.dbExecuteQuery(0, stmt);
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

    public static Student findStudent(int studentID) throws SQLException, ClassNotFoundException {

        String stmt = "select * from student where studentID = '" +studentID+"'";

        try {
            ResultSet rs = DatabaseConnection.dbExecuteQuery(0, stmt);

            Student student = getStudent(rs);

            return student;

        } catch (SQLException e) {
            System.out.println("Error while running findStudent in dao");
            throw e;
        }

    }

    public static Student getStudent(ResultSet rs) throws SQLException, ClassNotFoundException {
        Student tmp = new Student();

        if (rs.next()) {
            tmp.setStudentID(rs.getInt("studentID"));
            tmp.setFirstName(rs.getString("firstName"));
            tmp.setLastName(rs.getString("lastName"));
        }
        return tmp;
    }

    public static ObservableList<Student> findAllStudent() throws SQLException, ClassNotFoundException {
        String stmt = "select * from student";

        try {
            ResultSet rs = DatabaseConnection.dbExecuteQuery(0, stmt);

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
           tmp.setStudentID(rs.getInt("studentID"));
           tmp.setFirstName(rs.getString("firstName"));
           tmp.setLastName(rs.getString("lastName"));
           studentList.add(tmp);
       }
       return studentList;

    }

    public static ObservableList<String> getListStudents() throws SQLException, ClassNotFoundException {
        ObservableList<String> students = FXCollections.observableArrayList();
        students.add("Register student");
        for(Student s : findAllStudent()) {
            students.add(Integer.toString(s.getStudentID()));
        }
        return students;
    }


    public static int addStudent(String fName, String lName) throws SQLException, ClassNotFoundException{
        String stmt =
                "insert into Student VALUES('"+fName+"', '"+lName+"');";
        int id = 0;
        String stmt2 = "select max(studentID) from student";

        try {
            DatabaseConnection.dbExecuteUpdate(0, stmt);
            ResultSet rs = DatabaseConnection.dbExecuteQuery(0,stmt2);
            while (rs.next()) {
               id = rs.getInt("studentID");
            }

        } catch (SQLException e) {
            System.out.println("Error while inserting student");
            throw e;
        }
        return id;
    }

    public static void updateStudent(int studentID, String fName, String lName) throws SQLException, ClassNotFoundException {

        String stmt = "update student set firstName = '" + fName + "', lastName = '" + lName + "' where studentID = '" + studentID + "'";

        try {
            DatabaseConnection.dbExecuteUpdate(0, stmt);
        } catch (SQLException e) {
            System.out.println("Error while updating student.");
            throw e;
        }
    }

    public static void removeStudent(int studentID) throws SQLException, ClassNotFoundException {

        String stmt = "delete from student where studentID = '"+studentID+"'";

        try {
            DatabaseConnection.dbExecuteUpdate(0, stmt);
        } catch (SQLException e) {
            System.out.println("Error while deleting student.");
            throw e;
        }
    }
    
    public static String getAllStudents() {
		 String stmt = "select * from Student";
		 return stmt;
	 }
    
    public static String getSpecificStudent(int sID) {
    	String stmt = "select * from Student where studentID="+sID;
    	return stmt;
    }

}
