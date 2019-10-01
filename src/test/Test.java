package test;

import java.sql.ResultSet;
import java.sql.SQLException;

import dal.StudiesDAO;
import database.DatabaseConnection;
import javafx.collections.ObservableList;
import models.Course;

public class Test {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        DatabaseConnection db = new DatabaseConnection();

        ObservableList<Course> clist = StudiesDAO.findAllStudiesForStudents(30);

        for(Course c : clist) {
            System.out.println(c.getCourseCode());
       }
    }

}
