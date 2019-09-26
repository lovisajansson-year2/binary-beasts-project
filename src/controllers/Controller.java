package controllers;

import dal.*;

import database.DatabaseConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import models.Course;
import models.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import java.sql.SQLException;

public class Controller {

    //Course
    @FXML
    private ComboBox<String> cbCourses;
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
    private ComboBox<String> cbStudent;
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
    private ComboBox<String> cbRegCourses;
    @FXML
    private ComboBox<String> cbRegStudents;
    @FXML
    private ComboBox<String> cbGrade;
    @FXML
    private Button btnRegAdd;
    @FXML
    private Button btnRegRemove;
    @FXML
    private Button btnRegGrade;
    @FXML
    private Label lblRegistration;


    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {
        cbStudent.setItems(StudentDAO.getListStudents());
        cbStudent.getSelectionModel().selectFirst();
        cbCourses.setItems(CourseDAO.getListCourses());
        cbCourses.getSelectionModel().selectFirst();

        cbRegStudents.setItems(StudentDAO.getListStudents());
        cbRegStudents.getItems().remove(0);
        cbRegStudents.getItems().add(0, "Select student");
        cbRegStudents.getSelectionModel().selectFirst();
        cbRegCourses.setItems(CourseDAO.getListCourses());
        cbRegCourses.getItems().remove(0);
        cbRegCourses.getItems().add(0, "Select course");
        cbRegCourses.getSelectionModel().selectFirst();

        ObservableList<String> grades = FXCollections.observableArrayList("Select grade","F","E","D","C","B","A");
        cbGrade.setItems(grades);
        cbGrade.getSelectionModel().selectFirst();
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
        cbStudent.setItems(StudentDAO.getListStudents());
        cbStudent.getSelectionModel().selectLast();

    }

    @FXML
    private void updateStudent(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        try {
            StudentDAO.updateStudent(Integer.parseInt(cbStudent.getSelectionModel().getSelectedItem()), tfFirstName.getText(), tfLastName.getText());
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
            StudentDAO.removeStudent(Integer.parseInt(cbStudent.getSelectionModel().getSelectedItem()));
            lblStudents.setText("Message: Removed " + cbStudent.getSelectionModel().getSelectedItem());
            System.out.print("Student removed.");
        } catch(SQLException e) {
            lblStudents.setText("Message: Remove failed.");
            throw e;
        }
        cbStudent.setItems(StudentDAO.getListStudents());
        cbStudent.getSelectionModel().selectFirst();
        tfFirstName.setText("");
        tfLastName.setText("");
    }

    @FXML
    private void addCourse(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        try {
            CourseDAO.addCourse(Integer.parseInt(tfCredits.getText()));
            lblStudents.setText("Message: Added!");
            System.out.println("Course added.");
        } catch (SQLException e) {
            lblStudents.setText("Message: Registration failed.");
            throw e;
        }
        cbCourses.setItems(CourseDAO.getListCourses());
        cbStudent.getSelectionModel().selectLast();

    }

    @FXML
    private void updateCourse(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        try {
            CourseDAO.updateCourse(Integer.parseInt(cbCourses.getSelectionModel().getSelectedItem()), Integer.parseInt(tfCredits.getText()));
            lblStudents.setText("Message: Updated.");
            System.out.println("Course updated");
        } catch(SQLException e) {
            lblStudents.setText("Message: Update failed.");
            throw e;
        }
    }

    @FXML
    private void removeCourse(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        try {
            CourseDAO.removeCourse(Integer.parseInt(cbCourses.getSelectionModel().getSelectedItem()));
            lblCourses.setText("Message: Removed " + cbCourses.getSelectionModel().getSelectedItem());
            System.out.print("Course removed.");
        } catch(SQLException e) {
            lblStudents.setText("Message: Remove failed.");
            throw e;
        }
        cbCourses.setItems(CourseDAO.getListCourses());
        cbCourses.getSelectionModel().selectFirst();
        tfFirstName.setText("");
        tfLastName.setText("");
    }

    @FXML
    private void addRegistration(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        try {
            StudiesDAO.addStudies(Integer.parseInt(cbRegStudents.getSelectionModel().getSelectedItem()), Integer.parseInt(cbRegCourses.getSelectionModel().getSelectedItem()));
            lblRegistration.setText("Message: Registrated.");
            System.out.println("Registration updated");
        } catch(SQLException e) {
            lblRegistration.setText("Message: Registration failed.");
            throw e;
        }
    }

    @FXML
    private void removeRegistration(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        try {
            StudiesDAO.removeStudies(Integer.parseInt(cbRegStudents.getSelectionModel().getSelectedItem()), Integer.parseInt(cbRegCourses.getSelectionModel().getSelectedItem()));
            lblRegistration.setText("Message: Removed registration.");
            System.out.println("Registration removed");
        } catch(SQLException e) {
            lblRegistration.setText("Message: Removing failed.");
            throw e;
        }
    }

    @FXML
    private void setGrade(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        try {
            HasStudiedDAO.addHasStudied(Integer.parseInt(cbRegStudents.getSelectionModel().getSelectedItem()), Integer.parseInt(cbRegCourses.getSelectionModel().getSelectedItem()), cbGrade.getSelectionModel().getSelectedItem());
            lblRegistration.setText("Message: Removed registration.");
            System.out.println("Registration removed");
        } catch(SQLException e) {
            lblRegistration.setText("Message: Removing failed.");
            throw e;
        }
    }
}
