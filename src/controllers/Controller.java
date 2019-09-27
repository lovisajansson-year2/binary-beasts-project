package controllers;

import dal.*;

import database.DatabaseConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.Course;
import models.Student;
import models.Studies;
import models.HasStudied;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Controller {

    //Course
    @FXML
    private ComboBox<Course> cbCourses;
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
    private ComboBox<Course> cbRegCourses;
    @FXML
    private ComboBox<Student> cbRegStudents;
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
    
    //Overview
    private TableView<Student> hasStudiedTable;
    private TableColumn<String, Student> column1 = new TableColumn<>("");
    private TableColumn<String, Student> column2 = new TableColumn<>("");
    private TableColumn<String, Student> column3 = new TableColumn<>("");
    private TableColumn<String, Student> column4 = new TableColumn<>("");
    private ComboBox<Course> cbOverCourses;
    private ComboBox<Student> cbOverStudents;

    


    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {
        cbStudent.setItems(StudentDAO.findAllStudent());
        cbCourses.setItems(CourseDAO.findAllCourses());
        cbRegStudents.setItems(StudentDAO.findAllStudent());
        cbRegCourses.setItems(CourseDAO.findAllCourses());

        ObservableList<String> grades = FXCollections.observableArrayList("F","E","D","C","B","A");
        cbGrade.setItems(grades);
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
            CourseDAO.addCourse(Integer.parseInt(tfCredits.getText()));
            lblStudents.setText("Message: Added!");
            System.out.println("Course added.");
        } catch (SQLException e) {
            lblStudents.setText("Message: Registration failed.");
            throw e;
        }
        cbCourses.setItems(CourseDAO.findAllCourses());
        cbStudent.getSelectionModel().selectLast();

    }

    @FXML
    private void updateCourse(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        try {
            CourseDAO.updateCourse(cbCourses.getSelectionModel().getSelectedItem().getCourseCode(), Integer.parseInt(tfCredits.getText()));
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
            CourseDAO.removeCourse(cbCourses.getSelectionModel().getSelectedItem().getCourseCode());
            lblCourses.setText("Message: Removed " + cbCourses.getSelectionModel().getSelectedItem().getCourseCode());
            System.out.print("Course removed.");
        } catch(SQLException e) {
            lblStudents.setText("Message: Remove failed.");
            throw e;
        }
        cbCourses.setItems(CourseDAO.findAllCourses());
        cbCourses.getSelectionModel().selectFirst();
        tfFirstName.setText("");
        tfLastName.setText("");
    }

    @FXML
    private void addRegistration(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        try {
            StudiesDAO.addStudies(cbRegStudents.getSelectionModel().getSelectedItem().getStudentID(), cbRegCourses.getSelectionModel().getSelectedItem().getCourseCode());
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
            StudiesDAO.removeStudies(cbRegStudents.getSelectionModel().getSelectedItem().getStudentID(), cbRegCourses.getSelectionModel().getSelectedItem().getCourseCode());
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
            HasStudiedDAO.addHasStudied(cbRegStudents.getSelectionModel().getSelectedItem().getStudentID(), cbRegCourses.getSelectionModel().getSelectedItem().getCourseCode(), cbGrade.getSelectionModel().getSelectedItem());
            lblRegistration.setText("Message: Removed registration.");
            System.out.println("Registration removed");
        } catch(SQLException e) {
            lblRegistration.setText("Message: Removing failed.");
            throw e;
        }
    }
    
    @FXML
    private void buildHasStudiedTable(ObservableList<Student> hasStudiedData) throws ClassNotFoundException {
    	hasStudiedTable.setItems(hasStudiedData);	    		
    }
    
    private void buildAllCompletedStudents() throws SQLException, ClassNotFoundException{
    	int courseCode = cbOverCourses.getSelectionModel().getSelectedItem().getCourseCode();
    	try {
        	ObservableList<Student> hasList = HasStudiedDAO.findAllCompletedStudents(courseCode);
        	hasStudiedTable.setItems(hasList);
        	column1.setText("Student ID");
        	column2.setText("First Name");
        	column3.setText("Last Name");
        	column4.setText("Grade");
            
    	} catch(SQLException e) {
    		throw e;
    	}
    }
    
    private void buildData() throws SQLException, ClassNotFoundException {
    	ObservableList<ObservableList> data;
    	data = FXCollections.observableArrayList();
    	int courseCode = cbOverCourses.getSelectionModel().getSelectedItem().getCourseCode();
    	try {
    		String sql = "SELECT * FROM HasStudied WHERE courseCode ="+courseCode+"";
    		ResultSet rs = DatabaseConnection.dbExecuteQuery(sql);
    		
    		for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
    			final int j = i;                
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i));
                
    		}
    		
    	}catch(SQLException e) {
    		throw e;
    	}
    	
    }
}
