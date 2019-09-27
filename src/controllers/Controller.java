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
    private TableView tableView;
    @FXML
    private ComboBox<String> cbQ;
    @FXML
    private Button btnA1;



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

        ObservableList<String> questions = FXCollections.observableArrayList("0.1","0.2","0.3","0.4","0.5","0.6","0.7",
        "1","2","3","4","5","6");

        cbQ.setItems(questions);
        cbQ.getSelectionModel().selectFirst();

        listenerStudent();
    }

    @FXML
    private void buildData(int index) throws SQLException, ClassNotFoundException {
        ObservableList data = FXCollections.observableArrayList();
        String stmt = "";
         if(index == 0) {
            stmt = "SELECT No_, [First Name], [Last Name], Address, City\n" +
                    "FROM [CRONUS Sverige AB$Employee]\n";
        } else if(index == 1) {
             stmt = "SELECT [Employee No_], [From Date], [To Date], Description, Quantity\n" +
                     "FROM [CRONUS Sverige AB$Employee Absence]\n";
         } else if(index == 2) {
             stmt = "SELECT timestamp, [Search Limit], [Temp_ Key Index], [Temp_ Table No_], [Temp_ Option Value]\n" +
                     "FROM [CRONUS Sverige AB$Employee Portal Setup]\n";
         } else if(index == 3) {
             stmt = "SELECT [Employee No_], [Qualification Code], Description, Institution_Company, Cost\n" +
                     "FROM [CRONUS Sverige AB$Employee Qualification]\n";
         } else if(index == 4) {
             stmt = "SELECT [Employee No_], [Relative Code], [First Name], [Last Name], [Birth Date]\n" +
                     "FROM [CRONUS Sverige AB$Employee Relative]\n";
         } else if(index == 5) {
             stmt = "SELECT *\n" +
                     "FROM [CRONUS Sverige AB$Employee Statistics Group]\n";
         } else if(index == 6) {
             stmt = "SELECT *\n" +
                     "FROM [CRONUS Sverige AB$Employment Contract]\n";
         } else if(index == 7) {
             stmt = "SELECT name, type FROM sys.key_constraints;";
         } else if(index == 8) {
             stmt = "SELECT CONSTRAINT_NAME, CONSTRAINT_TYPE\n" +
                     "FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS";
         } else if(index == 9) {
             stmt = "SELECT TABLE_NAME\n" +
                     "FROM INFORMATION_SCHEMA.TABLES\n";
         } else if(index == 10) {
             stmt = "SELECT COLUMN_NAME\n" +
                     "FROM INFORMATION_SCHEMA.COLUMNS\n" +
                     "WHERE TABLE_NAME = 'CRONUS Sverige AB$Employee'\n";
         } else if(index == 11) {
             stmt = "";
         } else if(index == 12) {
             stmt = "SELECT TOP 1\n" +
                     "    [TableName] = o.name, \n" +
                     "    [RowCount] = i.rows\n" +
                     "FROM \n" +
                     "    sysobjects o, \n" +
                     "    sysindexes i \n" +
                     "WHERE \n" +
                     "    o.xtype = 'U' \n" +
                     "    AND \n" +
                     "    i.id = OBJECT_ID(o.name)\n" +
                     "ORDER BY i.rows DESC\n";
         }

        try {
            ResultSet rs = DatabaseConnection.dbExecuteQuery(1, stmt);

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
    public void listenerStudent() throws SQLException, ClassNotFoundException {
        cbStudent.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                try
                {
                    if(cbStudent.getSelectionModel().getSelectedIndex() != 0) {
                        tfFirstName.setText(StudentDAO.findStudent(Integer.parseInt(newValue)).getFirstName());
                        tfLastName.setText(StudentDAO.findStudent(Integer.parseInt(newValue)).getLastName());
                    } else {
                        tfFirstName.setText("");
                        tfLastName.setText("");
                    }
                } catch (SQLException e) {

                } catch (ClassNotFoundException e) {

                } catch (NullPointerException e) {

                }
            }
        });
    }

    @FXML
    public void listenerCourse() throws SQLException, ClassNotFoundException {
        cbStudent.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                try
                {
                    if(cbCourses.getSelectionModel().getSelectedIndex() != 0) {
                        tfCredits.setText(Integer.toString(CourseDAO.findCourse(Integer.parseInt(newValue)).getCredits()));

                    } else {
                        tfCredits.setText("");
                    }
                } catch (SQLException e) {

                } catch (ClassNotFoundException e) {

                } catch (NullPointerException e) {

                }
            }
        });
    }

    @FXML
    private void getAnswer(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        int index = cbQ.getSelectionModel().getSelectedIndex();
        tableView.getItems().clear();
        tableView.getColumns().clear();
        buildData(index);
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

        int sID = Integer.parseInt(cbRegStudents.getSelectionModel().getSelectedItem());
        int credits = 0;
        try {
            for(Course c : StudiesDAO.findAllStudiesForStudents(sID)) {
              credits =+ c.getCredits();
            }
            if(credits <= 45) {
                StudiesDAO.addStudies(sID, Integer.parseInt(cbRegCourses.getSelectionModel().getSelectedItem()));
                lblMessage.setText("Message: Registrated.");
                System.out.println("Registration updated");
            } else {
                lblMessage.setText("Message: Too many credits");
            }

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
}
