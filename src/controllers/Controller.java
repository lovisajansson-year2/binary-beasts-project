package controllers;

import dal.*;

import database.DatabaseConnection;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.*;
import javafx.util.Callback;
import models.Course;
import models.Student;
import models.Studies;
import models.HasStudied;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;

import java.sql.ResultSet;
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
    private Label lblMessage;

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
    
    //Overview
    private TableView<Student> hasStudiedTable;
    private TableColumn<String, Student> column1 = new TableColumn<>("");
    private TableColumn<String, Student> column2 = new TableColumn<>("");
    private TableColumn<String, Student> column3 = new TableColumn<>("");
    private TableColumn<String, Student> column4 = new TableColumn<>("");
    private ComboBox<Course> cbOverCourses;
    private ComboBox<Student> cbOverStudents;

    
    private TableView tableView;



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
        buildData();
    }

    @FXML
    private void buildData() throws SQLException, ClassNotFoundException {
        ObservableList data = FXCollections.observableArrayList();
        String stmt = "select * from studies";
        try {
            ResultSet rs = DatabaseConnection.dbExecuteQuery(stmt);

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tableView.getColumns().addAll(col);
                System.out.println("Column [" + i + "] ");
            }

            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added " + row);
                data.add(row);

            }
            tableView.setItems(data);
        } catch(SQLException e) {
            throw e;
        }

    }

    @FXML
    private void addStudent(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        try {
            StudentDAO.addStudent(tfFirstName.getText(), tfLastName.getText());
            lblMessage.setText("Message: Added!");
            System.out.println("Student added.");
        } catch (SQLException e) {
            lblMessage.setText("Message: Registration failed.");
            throw e;
        }
        cbStudent.setItems(StudentDAO.getListStudents());
        cbStudent.getSelectionModel().selectLast();

    }

    @FXML
    private void updateStudent(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        try {
            StudentDAO.updateStudent(Integer.parseInt(cbStudent.getSelectionModel().getSelectedItem()), tfFirstName.getText(), tfLastName.getText());
            lblMessage.setText("Message: Updated.");
            System.out.println("Student updated");
        } catch(SQLException e) {
            lblMessage.setText("Message: Update failed.");
            throw e;
        }
    }

    @FXML
    private void removeStudent(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        try {
            StudentDAO.removeStudent(Integer.parseInt(cbStudent.getSelectionModel().getSelectedItem()));
            lblMessage.setText("Message: Removed " + cbStudent.getSelectionModel().getSelectedItem());
            System.out.print("Student removed.");
        } catch(SQLException e) {
            lblMessage.setText("Message: Remove failed.");
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
            lblMessage.setText("Message: Added!");
            System.out.println("Course added.");
        } catch (SQLException e) {
            lblMessage.setText("Message: Registration failed.");
            throw e;
        }
        cbCourses.setItems(CourseDAO.getListCourses());
        cbStudent.getSelectionModel().selectLast();

    }

    @FXML
    private void updateCourse(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        try {
            CourseDAO.updateCourse(Integer.parseInt(cbCourses.getSelectionModel().getSelectedItem()), Integer.parseInt(tfCredits.getText()));
            lblMessage.setText("Message: Updated.");
            System.out.println("Course updated");
        } catch(SQLException e) {
            lblMessage.setText("Message: Update failed.");
            throw e;
        }
    }

    @FXML
    private void removeCourse(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        try {
            CourseDAO.removeCourse(Integer.parseInt(cbCourses.getSelectionModel().getSelectedItem()));
            lblMessage.setText("Message: Removed " + cbCourses.getSelectionModel().getSelectedItem());
            System.out.print("Course removed.");
        } catch(SQLException e) {
            lblMessage.setText("Message: Remove failed.");
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
            lblMessage.setText("Message: Registrated.");
            System.out.println("Registration updated");
        } catch(SQLException e) {
            lblMessage.setText("Message: Registration failed.");
            throw e;
        }
    }

    @FXML
    private void removeRegistration(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        try {
            StudiesDAO.removeStudies(Integer.parseInt(cbRegStudents.getSelectionModel().getSelectedItem()), Integer.parseInt(cbRegCourses.getSelectionModel().getSelectedItem()));
            lblMessage.setText("Message: Removed registration.");
            System.out.println("Registration removed");
        } catch(SQLException e) {
            lblMessage.setText("Message: Removing failed.");
            throw e;
        }
    }

    @FXML
    private void setGrade(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        try {
            HasStudiedDAO.addHasStudied(Integer.parseInt(cbRegStudents.getSelectionModel().getSelectedItem()), Integer.parseInt(cbRegCourses.getSelectionModel().getSelectedItem()), cbGrade.getSelectionModel().getSelectedItem());
            lblMessage.setText("Message: Removed registration.");
            System.out.println("Registration removed");
        } catch(SQLException e) {
            lblMessage.setText("Message: Removing failed.");
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
}
