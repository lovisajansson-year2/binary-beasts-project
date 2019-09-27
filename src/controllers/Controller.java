package controllers;

import dal.*;


import database.DatabaseConnection;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.util.Callback;
import models.Course;
import models.Student;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;

import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.*;



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
        	int index = cbStudent.getSelectionModel().getSelectedIndex();
        	if(index==0 && !tfFirstName.getText().equals("") && !tfLastName.getText().equals("")) {
            StudentDAO.addStudent(tfFirstName.getText(), tfLastName.getText());
            lblMessage.setText("Message: Student Added");
        	} else if(tfFirstName.getText()=="") {
        		lblMessage.setText("Message: Student must have a firstname");
        	} else if(tfLastName.getText()=="") {
        		lblMessage.setText("Message: Student must have a lastname");
        	} else {
        		lblMessage.setText("Message: You must choose 'Register new Student'");
        	}
     
        }finally{
        cbStudent.setItems(StudentDAO.getListStudents());
        cbStudent.getSelectionModel().selectLast();
        tfFirstName.setText("");
        tfLastName.setText("");
        }
    }
    

    @FXML
    private void updateStudent(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        try {
        	int index = cbStudent.getSelectionModel().getSelectedIndex();
            if(index!=0 && !tfFirstName.getText().equals("") && !tfLastName.getText().equals("")) {
        	StudentDAO.updateStudent(Integer.parseInt(cbStudent.getSelectionModel().getSelectedItem()), tfFirstName.getText(), tfLastName.getText());
            lblMessage.setText("Message: Student " + cbStudent.getSelectionModel().getSelectedItem() +" updated.");
        	} else if(tfFirstName.getText().equals("")) {
        		lblMessage.setText("Message: Student must have a firstname");
        	} else if(tfLastName.getText().equals("")) {
        		lblMessage.setText("Message: Student must have a lastname");
            } else {
            	lblMessage.setText("Message: You must select a student to update");
            }
        } finally {
            cbStudent.setItems(StudentDAO.getListStudents());
            cbStudent.getSelectionModel().selectLast();
            tfFirstName.setText("");
            tfLastName.setText("");
        }
    }

    @FXML
    private void removeStudent(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        try {
        	int index = cbStudent.getSelectionModel().getSelectedIndex();
        	if(index!=0) {
            StudentDAO.removeStudent(Integer.parseInt(cbStudent.getSelectionModel().getSelectedItem()));
            lblMessage.setText("Message: Removed " + cbStudent.getSelectionModel().getSelectedItem());
        	}else {
        		lblMessage.setText("Message: must pick a student to remove.");
        	}
        } catch(SQLException e) {
            lblMessage.setText("Message: Cannot remove student that is studying or has studied");

        }
        cbStudent.setItems(StudentDAO.getListStudents());
        cbStudent.getSelectionModel().selectFirst();
        tfFirstName.setText("");
        tfLastName.setText("");
    }

    @FXML
    private void addCourse(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        try {
        	int index = cbCourses.getSelectionModel().getSelectedIndex();
        	if (index==0) {
            CourseDAO.addCourse(Integer.parseInt(tfCredits.getText()));
            lblMessage.setText("Message: "+cbCourses.getSelectionModel().getSelectedItem()+" Added!");
        	} else {
        		lblMessage.setText("Message: You must choose 'Register new Course'");
        	}
        } catch (NumberFormatException e) {
        	lblMessage.setText("Message: course must have credits and they must be numbers");
        }
        cbCourses.setItems(CourseDAO.getListCourses());
        cbStudent.getSelectionModel().selectLast();
        

    }

    @FXML
    private void updateCourse(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        try {
        	int index = cbCourses.getSelectionModel().getSelectedIndex();
        	if(index!=0) {
            CourseDAO.updateCourse(Integer.parseInt(cbCourses.getSelectionModel().getSelectedItem()), Integer.parseInt(tfCredits.getText()));
            lblMessage.setText("Message: Updated.");
        	} else {
            	lblMessage.setText("Message: you must pick a course to update.");
            }

        } catch (NumberFormatException e) {
        	lblMessage.setText("Message: Course must have credits and they must be numbers");
        }
    }

    @FXML
    private void removeCourse(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        try {
        	int index = cbCourses.getSelectionModel().getSelectedIndex();
        	if(index!=0) {
            CourseDAO.removeCourse(Integer.parseInt(cbCourses.getSelectionModel().getSelectedItem()));
            lblMessage.setText("Message: Removed " + cbCourses.getSelectionModel().getSelectedItem());
            System.out.print("Course removed.");
        	} else {
        		lblMessage.setText("Message: You must pick a Course to delete.");
        	}
        } catch(SQLException e) {
            lblMessage.setText("Cannot delete course on which students are studying or has studied");
        }
        cbCourses.setItems(CourseDAO.getListCourses());
        cbCourses.getSelectionModel().selectFirst();
        tfFirstName.setText("");
        tfLastName.setText("");
    }

    @FXML
    private void addRegistration(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        try {
        	int index = cbRegStudents.getSelectionModel().getSelectedIndex();
        	if(index!=0) {
            StudiesDAO.addStudies(Integer.parseInt(cbRegStudents.getSelectionModel().getSelectedItem()), Integer.parseInt(cbRegCourses.getSelectionModel().getSelectedItem()));
            lblMessage.setText("Message: Registrated.");
       
        }
        }finally {
            cbRegStudents.getSelectionModel().selectFirst();
            cbRegCourses.getSelectionModel().selectFirst();
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
}
