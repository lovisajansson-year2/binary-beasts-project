package controllers;

import dal.StudentDAO;
import javafx.scene.control.Label;
import models.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import java.sql.SQLException;

public class Controller {

    @FXML
    private TextField tf1;
    @FXML
    private TextField tf2;
    @FXML
    private Button btnAdd;
    @FXML
    private Label lbl1;


    @FXML
    private void addStudent(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        try {
            StudentDAO.addStudent(tf1.getText(), tf2.getText());
            lbl1.setText("Added!");
            System.out.println("Student added.");
        } catch (SQLException e) {
            lbl1.setText("Failed");
            throw e;
        }
    }






}
