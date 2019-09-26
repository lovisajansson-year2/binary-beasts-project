package controllers;

import dal.StudentDAO;
import database.DatabaseConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import models.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import java.sql.SQLException;

public class Controller {

    //Course
    @FXML
    private ComboBox cbCourses;
    @FXML
    private TextField tfCredits;
    @FXML
    private Button btnCoursesAdd;
    @FXML
    private Button btnCoursesUpdate;
    @FXML
    private Button btnCoursesRemove;
    @FXML
    private Label lblCourses;

    //Student
    @FXML
    private ComboBox<Student> cbStudent;
    @FXML
    private TextField tfFirstName;
    @FXML
    private TextField tfLastName;
    @FXML
    private Button btnStudentAdd;
    @FXML
    private Button btnStudentUpdate;
    @FXML
    private Button btnStudentRemove;
    @FXML
    private Label lblStudents;

    //Registration
    @FXML
    private Label lblRegistration;


    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {
        cbStudent.setItems(StudentDAO.findAllStudent());
    }


    @FXML
    private void addStudent(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        try {
            StudentDAO.addStudent(tfFirstName.getText(), tfLastName.getText());
            lblStudents.setText("Message: Added!");
            System.out.println("Student added.");
        } catch (SQLException e) {
            lblStudents.setText("Message: Registration failed.");
            throw e;
        }
        cbStudent.setItems(StudentDAO.findAllStudent());
        cbStudent.getSelectionModel().selectLast();

    }

    @FXML
    private void updateStudent(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        try {
            StudentDAO.updateStudent(cbStudent.getSelectionModel().getSelectedItem().getStudentID(), tfFirstName.getText(), tfLastName.getText());
            lblStudents.setText("Message: Updated.");
            System.out.println("Student updated");
        } catch(SQLException e) {
            lblStudents.setText("Message: Update failed.");
            throw e;
        }
    }

    @FXML
    private void removeStudent(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        try {
            StudentDAO.removeStudent(cbStudent.getSelectionModel().getSelectedItem().getStudentID());
            lblStudents.setText("Message: Removed " + cbStudent.getSelectionModel().getSelectedItem().getStudentID());
            System.out.print("Student removed.");
        } catch(SQLException e) {
            lblStudents.setText("Message: Remove failed.");
            throw e;
        }
        cbStudent.setItems(StudentDAO.findAllStudent());
        cbStudent.getSelectionModel().selectFirst();
        tfFirstName.setText("");
        tfLastName.setText("");
    }

    @FXML
    private void addCourse(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        try {
            CourseDAO.addCourse(tfCredits.getInt());
            lblStudents.setText("Message: Added!");
            System.out.println("Student added.");
        } catch (SQLException e) {
            lblStudents.setText("Message: Registration failed.");
            throw e;
        }
        cbStudent.setItems(StudentDAO.findAllStudent());
        cbStudent.getSelectionModel().selectLast();

    }
}
