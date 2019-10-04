package controllers;

import dal.*;
import models.Course;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.*;



public class Controller {

    //Course
    @FXML
    private ComboBox<String> cbCourses;
    @FXML
    private TextField tfCredits;
    @FXML
    private Label lblMessage;

    //Student
    @FXML
    private ComboBox<String> cbStudent;
    @FXML
    private TextField tfFirstName;
    @FXML
    private TextField tfLastName;

    //Registration
    @FXML
    private ComboBox<String> cbRegCourses;
    @FXML
    private ComboBox<String> cbRegStudents;
    @FXML
    private ComboBox<String> cbGrade;
    @FXML
    private TableView tvRegistration;

    //Overview
    @FXML
    private TextField tfSearch;
    @FXML
    private ComboBox<String> cbSearch;
    @FXML
    private ComboBox<String> cbFilter;
    @FXML
    private TableView tvOverview;
    @FXML
    private Label lblError;


    @FXML
    private TableView tableView;
    @FXML
    private ComboBox<String> cbQ;

    private void resetFields() {
      	 cbRegStudents.getSelectionModel().selectFirst();
      	 cbRegCourses.getSelectionModel().selectFirst();
      	 cbGrade.getSelectionModel().selectFirst();
      	 cbStudent.getSelectionModel().selectFirst();
      	 cbCourses.getSelectionModel().selectFirst();
      	 tfFirstName.setText("");
      	 tfLastName.setText("");
    } 
    

    private static int getID(ComboBox<String> comboBoxName ) {
       	try{return Integer.parseInt(comboBoxName.getSelectionModel().getSelectedItem());
	       }catch(NumberFormatException e) {
	    	   return 0;
	       }
     }
    
    private static String getItem(ComboBox<String> comboboxName) {
    	return comboboxName.getSelectionModel().getSelectedItem();
    }
    
    @FXML
    private void initialize() {
        try {
        // Stu & Cou
        cbStudent.setItems(StudentDAO.getListStudents());
        cbStudent.getSelectionModel().selectFirst();
        cbCourses.setItems(CourseDAO.getListCourses());
        cbCourses.getSelectionModel().selectFirst();

        //Registration
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

        buildData(0,tvRegistration, Assignment2DAO.getRegistrations());

        //Overview
        ObservableList<String> search = FXCollections.observableArrayList("Student", "Course", "Studies");
        cbSearch.setItems(search);
        cbSearch.getSelectionModel().selectFirst();
        ObservableList<String> filter1 = FXCollections.observableArrayList("None");
        cbFilter.setItems(filter1);
        cbFilter.getSelectionModel().selectFirst();
        lblError.setText("Message:");
        tfSearch.setText("");
        getResult();
        
        //Querys
        ObservableList<String> questions = FXCollections.observableArrayList(
                "0.1 TableCRONUS Sverige AB$Employee",
                "0.2 Table CRONUS Sverige AB$Employee Absence",
                "0.3 Table CRONUS Sverige AB$Employee Portal Setup",
                "0.4 Table CRONUS Sverige AB$Employee Qualification",
                "0.5 Table CRONUS Sverige AB$Employee Relative",
                "0.6 Table CRONUS Sverige AB$Employee Statistics Group",
                "0.7 Table CRONUS Sverige AB$Employment Contract",
                "1.1 All keys", "1.2 All table constraints",
                "1.3 All tables","1.4 All columns in 'Employee'",
                "1.5 Metadata for 'Employee'",
                "1.6 Table with most rows","2.1 How much is 100NOK?","2.2 What value is the most expensive?",
                "2.3 Fotograferna AB's address","2.4 Name of employees that have been ill",
                "2.5 Family relations","2.6 Andreas B's customers",
                "2.7 Bank accounts beloning to CuNO 10,000");
        cbQ.setItems(questions);
        cbQ.getSelectionModel().selectFirst();

        listenerStudent();
        listenerCourse();
        listenerOverview();
        listenerRegistration();
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e);
            lblMessage.setText("Message: Connection to database failed contact admin.");
            lblError.setText("Message: Connection to database failed contact admin.");

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Database connection failed");
            alert.setContentText("Check that your database is correctly up and running.\n" +
            "Press ok to load the application without proper connection.");
            alert.showAndWait();
        } catch (ClassNotFoundException e) {

        }
    }

   

    private void buildData(int connection, TableView tableView, ResultSet rs) {
        ObservableList data = FXCollections.observableArrayList();
        try {

                tableView.getColumns().clear();
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
                }

                while (rs.next()) {
                    //Iterate Row
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        //Iterate Column
                        row.add(rs.getString(i));
                    }
                    data.add(row);

                }
                tableView.setItems(data);
            
        } catch(SQLException e) {
            lblError.setText("Message: Connection to database failed contact admin.");
        }

    }

    @FXML
    public void onEnter(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        getResult();
    }

    @FXML
    public void listenerStudent() {
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
                
                } catch (ClassNotFoundException e) {

                } catch (NullPointerException e) {
                    
				} catch (SQLException e) {
                    System.out.println("Connection failed: " + e);
                    lblMessage.setText("Message: Message: Connection to database failed contact admin.");
                    lblError.setText("Message: Message: Connection to database failed contact admin.");
				}
            }
        });
    }

    @FXML
    public void listenerCourse() {
        cbCourses.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
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
                    System.out.println("Connection failed: " + e);
                    lblMessage.setText("Message: Connection to database failed contact admin.");
                    lblError.setText("Message: Connection to database failed contact admin.");
                } catch (ClassNotFoundException e) {

                } catch (NullPointerException e) {

                }
            }
        });
    }

    @FXML
    public void listenerRegistration() {
         cbRegCourses.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                try
                {
                    if(HasStudiedDAO.findGrade(getID(cbRegStudents),getID(cbRegCourses)) != null) {
                        cbGrade.getSelectionModel().select(HasStudiedDAO.findGrade(getID(cbRegStudents),getID(cbRegCourses)));
                    } else if (HasStudiedDAO.findGrade(getID(cbRegStudents),getID(cbRegCourses)) == null) {
                        cbGrade.getSelectionModel().selectFirst();
                    } else {
                        cbGrade.getSelectionModel().selectFirst();
                    }

                } catch (ClassNotFoundException e) {

                } catch (NullPointerException e) {

                } catch (SQLException e) {
                    System.out.println("Connection failed: " + e);
                    lblMessage.setText("Message: Connection to database failed contact admin.");
                    lblError.setText("Message: Connection to database failed contact admin.");
                } catch (NumberFormatException e) {

                }
            }
        });
        cbRegStudents.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                try
                {

                    if(HasStudiedDAO.findGrade(getID(cbRegStudents),getID(cbRegCourses)) != null) {
                        cbGrade.getSelectionModel().select(HasStudiedDAO.findGrade(getID(cbRegStudents),getID(cbRegCourses)));
                    } else if (HasStudiedDAO.findGrade(getID(cbRegStudents),getID(cbRegCourses)) == null) {
                        cbGrade.getSelectionModel().selectFirst();
                    } else {
                        cbGrade.getSelectionModel().selectFirst();
                    }


                } catch (ClassNotFoundException e) {

                } catch (NullPointerException e) {

                } catch (SQLException e) {
                    System.out.println("Connection failed: " + e);
                    lblMessage.setText("Message: Connection to database failed contact admin.");
                    lblError.setText("Message: Connection to database failed contact admin.");
                } catch (NumberFormatException e) {

                }
            }
        });
    }

   @FXML 
   public void listenerOverview() {
        cbSearch.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                ObservableList<String> filter1 = FXCollections.observableArrayList("None");
                ObservableList<String> filter3 = FXCollections.observableArrayList("None", "Details");
                ObservableList<String> filter2 = FXCollections.observableArrayList("Ongoing", "Completed");
                try {
                  if (cbSearch.getSelectionModel().getSelectedIndex() == 0) {
                        cbFilter.setItems(filter1);
                        cbFilter.getSelectionModel().selectFirst();
                        getResult();
                        tfSearch.setPromptText("Search studentID");
                  } else if (cbSearch.getSelectionModel().getSelectedIndex() == 1) {
                      cbFilter.setItems(filter3);
                      cbFilter.getSelectionModel().selectFirst();
                      getResult();
                      tfSearch.setPromptText("Search courseCode");
                  } else if (cbSearch.getSelectionModel().getSelectedIndex() == 2) {
                      cbFilter.setItems(filter2);
                      cbFilter.getSelectionModel().selectFirst();
                      getResult();
                      tfSearch.setPromptText("Search courseCode");
                  }
                  
                } catch (NullPointerException e) {

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
       cbFilter.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
           @Override
           public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
               try {
                   getResult();
               } catch (NullPointerException e) {

               } catch (SQLException e) {
                   e.printStackTrace();
               } catch (ClassNotFoundException e) {
                   e.printStackTrace();
               }

           }
       });
    }

    @FXML
    private void getAnswer(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        int index = cbQ.getSelectionModel().getSelectedIndex();
        tableView.getItems().clear();
        tableView.getColumns().clear();
        buildData(1, tableView, Assignment2DAO.getStmt(index));
    }

    @FXML
    private void getResult() throws SQLException, ClassNotFoundException {
        ResultSet rs;
        try {
            if(getItem(cbSearch).equals("Student") && cbFilter.getSelectionModel().getSelectedIndex() == 0 && tfSearch.getText().equals("")) {
                rs = StudentDAO.getAllStudents();
                tvOverview.getColumns().clear();
                buildData(0, tvOverview, rs);
                lblError.setText("Message: Shows all students");
            } else if(getItem(cbSearch).equals("Student") && cbFilter.getSelectionModel().getSelectedIndex() == 0 && searchGetID() != 0) {
                rs = StudentDAO.getSpecificStudent(searchGetID());
                tvOverview.getColumns().clear();
                buildData(0, tvOverview, rs);
                ObservableList<String> list = tvOverview.getItems();
                if (!list.isEmpty()) {
                    lblError.setText("Message: Shows student "+searchGetID()+"");
                } else {
                    lblError.setText("Message: The student does not exist.");
                }
            } else if(getItem(cbSearch).equals("Course") && cbFilter.getSelectionModel().getSelectedIndex() == 0 && tfSearch.getText().equals("")) {
                rs = CourseDAO.getAllCourses();
                tvOverview.getColumns().clear();
                buildData(0, tvOverview, rs);
                lblError.setText("Message: Shows all courses");
            } else if(getItem(cbSearch).equals("Course") && cbFilter.getSelectionModel().getSelectedIndex() == 1 && tfSearch.getText().equals("")) {
                rs = CourseDAO.getCoursesDetailed();
                tvOverview.getColumns().clear();
                buildData(0, tvOverview, rs);
                lblError.setText("Message: Shows all courses with details");
            } else if(getItem(cbSearch).equals("Course") && cbFilter.getSelectionModel().getSelectedIndex() == 0 && searchGetID() != 0) {
                rs = CourseDAO.getSpecificCourse(searchGetID());
                tvOverview.getColumns().clear();
                buildData(0, tvOverview, rs);
                ObservableList<String> list = tvOverview.getItems();
                if (!list.isEmpty()) {
                    lblError.setText("Message: Shows course "+searchGetID()+"");
                } else {
                    lblError.setText("Message: The course does not exist.");
                }
            } else if(getItem(cbSearch).equals("Course") && cbFilter.getSelectionModel().getSelectedIndex() == 1 && searchGetID() != 0) {
                rs = CourseDAO.getSpecificCourseDetailed(searchGetID());
                tvOverview.getColumns().clear();
                buildData(0, tvOverview, rs);
                ObservableList<String> list = tvOverview.getItems();
                if (!list.isEmpty()) {
                    lblError.setText("Message: Shows details for course " + searchGetID() + "");
                } else {
                    lblError.setText("Message: Details for this course does not exist.");
                }
            } else if(getItem(cbSearch).equals("Studies") && cbFilter.getSelectionModel().getSelectedIndex() == 0 && tfSearch.getText().equals("")) {
                rs = StudiesDAO.getAllStudies();
                tvOverview.getColumns().clear();
                buildData(0, tvOverview, rs);
                lblError.setText("Message: Shows all ongoing studies");
            } else if(getItem(cbSearch).equals("Studies") && cbFilter.getSelectionModel().getSelectedIndex() == 1 && tfSearch.getText().equals("")) {
                rs = HasStudiedDAO.getAllHasStudied();
                tvOverview.getColumns().clear();
                buildData(0, tvOverview, rs);
                lblError.setText("Message: Shows all completed studies");
            } else if((getItem(cbSearch).equals("Studies") && getItem(cbFilter).equals("Ongoing") && searchGetID() != 0)) {
                rs = StudiesDAO.getStartedStmt(searchGetID());
                tvOverview.getColumns().clear();
                buildData(0, tvOverview, rs);
                ObservableList<String> list = tvOverview.getItems();
                if (!list.isEmpty()) {
                    lblError.setText("Message: Shows all ongoing studies for course "+searchGetID()+"");
                } else {
                    lblError.setText("Message: The course does not exist.");
                }
            } else if(getItem(cbSearch).equals("Studies") && getItem(cbFilter).equals("Completed") && searchGetID() != 0) {
                rs = HasStudiedDAO.getCompletedStmt(searchGetID());
                tvOverview.getColumns().clear();
                buildData(0, tvOverview, rs);
                ObservableList<String> list = tvOverview.getItems();
                if (!list.isEmpty()) {
                    lblError.setText("Message: Shows all completed studies for course "+searchGetID()+"");
                } else {
                    lblError.setText("Message: The course does not exist.");
                }
            } else {
                lblError.setText("Message: Please choose an valid id");
            }
        } catch (NumberFormatException e) {
            lblError.setText("You must search for a number");
        } catch (SQLException e) {
            lblError.setText("Message: Connection to database failed contact admin.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void addStudent(ActionEvent actionEvent) {

        try {
        	int index = cbStudent.getSelectionModel().getSelectedIndex();

        	if(index==0 && !tfFirstName.getText().equals("") && !tfLastName.getText().equals("")) {
        	    int id = StudentDAO.addStudent(tfFirstName.getText(), tfLastName.getText());
        	    lblMessage.setText("Message: Student "+id+" added");
                cbStudent.getItems().add(Integer.toString(id));
                cbRegStudents.getItems().add(Integer.toString(id));
                resetFields();
            }else if(index!=0) {
        		lblMessage.setText("Message: This student already exists. Please select 'Register new student' ");
        	}else if(tfFirstName.getText().equals("")) {
        		lblMessage.setText("Message: Student must have a first name");
        	} else if(tfLastName.getText().equals("")) {
        		lblMessage.setText("Message: Student must have a last name");
        	
        	}
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
            System.out.println("Connection failed: " + e);
            lblMessage.setText("Message: Connection failed.");
            lblError.setText("Message: Connection failed.");
        	
        }
        
        
    }

    @FXML
    private void updateStudent(ActionEvent actionEvent) {

        try {
        	int index = cbStudent.getSelectionModel().getSelectedIndex();
            if(index!=0 && !tfFirstName.getText().equals("") && !tfLastName.getText().equals("")) {
        	StudentDAO.updateStudent(getID(cbStudent), tfFirstName.getText(), tfLastName.getText());
            lblMessage.setText("Message: Student " + getID(cbStudent)+" updated.");
            resetFields();
        	} else if(index==0){
            	lblMessage.setText("Message: You must select a student to update");
            } else if(tfFirstName.getText().equals("")) {
        		lblMessage.setText("Message: Student must have a first name");
        	} else if(tfLastName.getText().equals("")) {
        		lblMessage.setText("Message: Student must have a last name");

            }
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
            System.out.println("Connection failed: " + e);
            lblMessage.setText("Message: Connection failed.");
            lblError.setText("Message: Connection failed.");
        }
    }

    @FXML
    private void removeStudent(ActionEvent actionEvent)  {

        try {
        	int index = cbStudent.getSelectionModel().getSelectedIndex();
        	if(index!=0) {
        		StudentDAO.removeStudent(getID(cbStudent));
        		lblMessage.setText("Message: Removed student " + getID(cbStudent));
        		cbRegStudents.getItems().remove(cbStudent.getSelectionModel().getSelectedItem());
        		cbStudent.getItems().remove(cbStudent.getSelectionModel().getSelectedItem());
        	}else {
        		lblMessage.setText("Message: must pick a student to remove.");
        	}
        } catch(SQLException e) {
            lblMessage.setText("Message: Cannot remove student that is studying or has studied");

        } catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        resetFields();
    }

    @FXML
    private void addCourse(ActionEvent actionEvent)  {

        try {
        	int index = cbCourses.getSelectionModel().getSelectedIndex();
        	if (index==0) {
                int id = CourseDAO.addCourse(Integer.parseInt(tfCredits.getText()));
                lblMessage.setText("Message: Course "+id+" added.");
                cbCourses.getItems().add(Integer.toString(id));
                cbRegCourses.getItems().add(Integer.toString(id));

                resetFields();
             } else {
        		lblMessage.setText("Message: This course already exists. Please select 'Register new Course'");
        	}
        } catch (NumberFormatException e) {
        	lblMessage.setText("Message: course must have credits and they must be numbers");
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
            System.out.println("Connection failed: " + e);
            lblMessage.setText("Message: Connection to database failed contact admin.");
            lblError.setText("Message: Connection to database failed contact admin.");
		}

        

    }

    @FXML
    private void updateCourse(ActionEvent actionEvent)  {

        try {
        	int index = cbCourses.getSelectionModel().getSelectedIndex();
        	if(index!=0) {
            CourseDAO.updateCourse(getID(cbCourses), Integer.parseInt(tfCredits.getText()));
            lblMessage.setText("Message: Course "+getID(cbCourses)+" Updated.");
            resetFields();
        	} else {
            	lblMessage.setText("Message: you must pick a course to update.");
            }

        } catch (NumberFormatException e) {
        	lblMessage.setText("Message: Course must have credits and they must be numbers");
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
            System.out.println("Connection failed: " + e);
            lblMessage.setText("Message: Connection to database failed contact admin.");
            lblError.setText("Message: Connection to database failed contact admin.");
		}

    }

    @FXML
    private void removeCourse(ActionEvent actionEvent)  {

        try {
        	int index = cbCourses.getSelectionModel().getSelectedIndex();
        	if(index!=0) {
            CourseDAO.removeCourse(getID(cbCourses));
            lblMessage.setText("Message: Removed course " + getID(cbCourses));
            cbRegCourses.getItems().remove(cbCourses.getSelectionModel().getSelectedItem());
            cbCourses.getItems().remove(cbCourses.getSelectionModel().getSelectedItem());
            resetFields();
        	} else {
        		lblMessage.setText("Message: You must pick a Course to delete.");
        	}
        } catch(SQLException e) {
            lblMessage.setText("Cannot delete course on which students are studying or has studied");
        } catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
    }

    @FXML

    private void addRegistration(ActionEvent actionEvent) {
    	try {
    		int index = cbRegStudents.getSelectionModel().getSelectedIndex();
    		int index2 = cbRegCourses.getSelectionModel().getSelectedIndex();
    		boolean match = true;
    		if(index!=0 && index2!=0) {
		        int sID = getID(cbRegStudents);
		        int credits = 0;
		        for(Course c : StudiesDAO.findAllStudiesForStudents(sID)) {
		        	credits = credits + c.getCredits();
		        }
		        credits = credits + CourseDAO.findCourse(getID(cbRegCourses)).getCredits();
		        if(credits <= 45) {
		            for(Course c : HasStudiedDAO.findAllCompletedCourses(sID)) {
		                if(c.getCourseCode()==getID(cbRegCourses)) {
		                    match = false;
                        }
                    }
		            if(match) {
                        StudiesDAO.addStudies(sID,getID(cbRegCourses));
                        lblMessage.setText("Message: Registered " + getID(cbRegStudents) + " on course " + getID(cbRegCourses));
                        resetFields();
                        buildData(0, tvRegistration, Assignment2DAO.getRegistrations());
                    } else {
		                lblMessage.setText("Message: Student has already completed this course.");
                    }

		        } else {
	            lblMessage.setText("Message: Can't add registration, credits for student would be higher than 45.");
	            } 
    		} else if(index+index2==0) {
        		lblMessage.setText("Message: You have to pick a student and a course to add registration");
         	} else if(index==0) {
         		lblMessage.setText("Message: You have to pick a student to add registration.");
         	} else if(index2==0) {
         		lblMessage.setText("Message: You have to pick a course to add registration.");
         	}
    	}catch(NumberFormatException e) {
    		lblMessage.setText("You have to pick a student and a course to register studies");	
    	}catch(ClassNotFoundException e) {
	        e.printStackTrace();
	    } catch (SQLException e) {
			lblMessage.setText("Message: This student is already studying this course");
		}
    }

    @FXML
    private void removeRegistration(ActionEvent actionEvent) {
    	 try {
         	int index = cbRegStudents.getSelectionModel().getSelectedIndex();
         	int index2 = cbRegCourses.getSelectionModel().getSelectedIndex();
         	boolean match = false;
         	if(index!=0 && index2!=0) {

                int sID = getID(cbRegStudents);
                for(Course c : StudiesDAO.findAllStudiesForStudents(sID)) {
                    if(c.getCourseCode()==getID(cbRegCourses)) {

                        match = true;
                    }
                }
                if(match) {
                    StudiesDAO.removeStudies(getID(cbRegStudents), getID(cbRegCourses));
                    lblMessage.setText("Message: Removed student " + getID(cbRegStudents) + " from course " + getID(cbRegCourses));
                    resetFields();
                    buildData(0, tvRegistration, Assignment2DAO.getRegistrations());
                } else {
                    lblMessage.setText("Message: Student is not studying this course.");
                }
         	}else if(index+index2==0) {
        		lblMessage.setText("Message: You have to pick a student and a course to remove registration");
         	} else if(index==0) {
         		lblMessage.setText("Message: You have to pick a student to remove registration.");
         	} else if(index2==0) {
         		lblMessage.setText("Message: You have to pick a course to remove registration.");
         	}  
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
             System.out.println("Connection failed: " + e);
             lblMessage.setText("Message: Connection to database failed contact admin.");
             lblError.setText("Message: Connection to database failed contact admin.");
		}
	
     }

    @FXML
    private void setGrade(ActionEvent actionEvent) {
        try {

        	int index = cbRegStudents.getSelectionModel().getSelectedIndex();
        	int index2 = cbRegCourses.getSelectionModel().getSelectedIndex();
        	int index3 = cbGrade.getSelectionModel().getSelectedIndex();
        	if(index!=0 && index2!=0 && index3!=0) {
        		int sID = getID(cbRegStudents);
				HasStudiedDAO.addHasStudied(sID, getID(cbRegCourses), cbGrade.getSelectionModel().getSelectedItem());			
        		lblMessage.setText("Message: set Grade " + cbGrade.getSelectionModel().getSelectedItem()+" for Student "+getID(cbRegStudents)+" on course " + getID(cbRegCourses) );
        		StudiesDAO.removeStudies(sID, getID(cbRegCourses));
        		resetFields();
                buildData(0,tvRegistration, Assignment2DAO.getRegistrations());
        	}else if(index+index2+index3==0) {
        		lblMessage.setText("Message: You have to pick a student, a course and a grade to update grade");
        	} else if(index==0) {
        		lblMessage.setText("Message: You have to pick a student to update grade");
        	} else if(index2==0) {
        		lblMessage.setText("Message: You have to pick a course to update grade.");
        	} else if(index3==0) {
        		lblMessage.setText("Message: You have to pick a grade to update grade");
        	}
        } catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
       
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			lblMessage.setText("Message: This student already has a grade for this course");
	
        }
    }

    private int searchGetID() throws NumberFormatException  {
    	int vid =0;
		String value = tfSearch.getText();
		if(value.equals("")) {
			vid = 0;
			return 0;
		} else {
	    vid= Integer.parseInt(value);
	    return vid;	
		}
    }
    

    //Assignment 3
	
    @FXML
    public void openQExcel(ActionEvent actionEvent) {
    	File excelFile = new File("C:\\Users\\Administrator\\Desktop\\assignment3_files\\Assignment3-queries.xlsx");
    	
    	try {
			Desktop.getDesktop().open(excelFile);
		} catch (IOException e) {
			e.printStackTrace();
		 }catch (IllegalArgumentException e) {
			   System.out.println("File does not exist");
		   }
    }
    
   @FXML
   public void openQAccess(ActionEvent actionEvent) {
	   File accessFile = new File("C:\\Users\\Administrator\\Desktop\\assignment3_files\\Assignment3-queries.accdb");
	   
	   try {
		   Desktop.getDesktop().open(accessFile);
	   } catch (IOException e) {
		   e.printStackTrace();
	   }catch (IllegalArgumentException e) {
		   System.out.println("File does not exist");
	   }
   }
   
   @FXML
   public void openCRExcel(ActionEvent actionEvent) {
	   File excelCR = new File("C:\\Users\\Administrator\\Desktop\\assignment3_files\\Assignment3-Customer-Report.xlsx");
	   
	   try {
		   Desktop.getDesktop().open(excelCR);
	   } catch (IOException e) {
		   e.printStackTrace();
	   }catch (IllegalArgumentException e) {
		   System.out.println("File does not exist");
	   }
   }
   @FXML
   public void openERExcel (ActionEvent actionEvent) {
	   File excelER = new File("C:\\Users\\Administrator\\Desktop\\assignment3_files\\Assignment3-Employee-Report.xlsx");
	   
	   try {
		   Desktop.getDesktop().open(excelER);
	   } catch (IOException e) {
		   e.printStackTrace();
	   }catch (IllegalArgumentException e) {
		   System.out.println("File does not exist");
	   }
   }
   @FXML 
   public void openCRAccess(ActionEvent actionEvent) {
	   File accessCR = new File("C:\\Users\\Administrator\\Desktop\\assignment3_files\\Assignment3-Customer-Report.accdb");
	   
	   try {
		   Desktop.getDesktop().open(accessCR);
	   } catch (IOException e) {
		   e.printStackTrace();
	   }catch (IllegalArgumentException e) {
		   System.out.println("File does not exist");
	   }
   }
   @FXML
   public void openERAccess(ActionEvent actionEvent) {
	   File accessER = new File("C:\\Users\\Administrator\\Desktop\\assignment3_files\\Assignment3-Employee-Report.accdb");
			   
	   
	   try {
		   Desktop.getDesktop().open(accessER);
	   } catch (IOException e) {
		   e.printStackTrace();
	   }catch (IllegalArgumentException e) {
		   System.out.println("File does not exist");
	   }
   }

}
