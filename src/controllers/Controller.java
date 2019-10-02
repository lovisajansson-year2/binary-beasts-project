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
import javafx.fxml.FXML;
import javafx.event.ActionEvent;

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
       	return Integer.parseInt(comboBoxName.getSelectionModel().getSelectedItem());

     }
    
    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {
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

        buildData(0,tvRegistration,buildStatement(2));

        //Overview
        ObservableList<String> search = FXCollections.observableArrayList("Student", "Course", "Relation");
        cbSearch.setItems(search);
        cbSearch.getSelectionModel().selectFirst();
        ObservableList<String> filter1 = FXCollections.observableArrayList("None");
        cbFilter.setItems(filter1);
        cbFilter.getSelectionModel().selectFirst();
        
        //Querys
        ObservableList<String> questions = FXCollections.observableArrayList("0.1","0.2","0.3","0.4","0.5","0.6","0.7",
        "1.1 All keys","1.2 All table constraints","1.3 All tables","1.4 All columns in 'Employee'", "1.5 Metadata for 'Employee'",
                "1.6 Table with most rows","2.1 How much is 100NOK?","2.2 What value is the most expensive?",
                "2.3 Fotograferna AB's address","2.4 Name of employees that have been ill","2.5 Family relations","2.6 Andreas B's customers",
                "2.7 Bank accounts beloning to CuNO 10,000");
        cbQ.setItems(questions);
        cbQ.getSelectionModel().selectFirst();

        listenerStudent();
        listenerCourse();
        listenerOverview();
    }

   

    private void buildData(int connection, TableView tableView, String stmt) throws SQLException, ClassNotFoundException {
        ObservableList data = FXCollections.observableArrayList();

        try {
            ResultSet rs = DatabaseConnection.dbExecuteQuery(connection, stmt);
            
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
                System.out.println("Column [" + i + "] ");
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
            //throw e;
        }

    }

    @FXML
    public void onEnter(ActionEvent actionEvent) throws SQLException, ClassNotFoundException{
        //getResult();
    	tvOverview.getItems().clear();
    	//buildCourseResultTable();
        if (cbFilter.getSelectionModel().getSelectedItem().equals("Started")) {
            buildData(0,tvOverview, buildStatement(0));
        } else if (cbFilter.getSelectionModel().getSelectedItem().equals("Completed")) {
        	buildData(0,tvOverview, buildStatement(1));
        } else if (cbFilter.getSelectionModel().getSelectedItem().equals("Throughput")) {
        	 // does not work
        }
        else {
    		lblError.setText("Please choose on item from the filter list."); // does not work
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
                
                } catch (ClassNotFoundException e) {

                } catch (NullPointerException e) {
                    
				} catch (SQLException e) {
					
				}
            }
        });
    }

    @FXML
    public void listenerCourse() throws SQLException, ClassNotFoundException {
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

                } catch (ClassNotFoundException e) {

                } catch (NullPointerException e) {

                }
            }
        });
    }

   @FXML 
   public void listenerOverview() throws SQLException, ClassNotFoundException {
        cbSearch.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ObservableList<String> filter1 = FXCollections.observableArrayList("None");
                ObservableList<String> filter2 = FXCollections.observableArrayList("Started", "Completed");
                try {
                  if (cbSearch.getSelectionModel().getSelectedIndex() == 0) {
                        cbFilter.setItems(filter1);
                        cbFilter.getSelectionModel().selectFirst();
                  } else if (cbSearch.getSelectionModel().getSelectedIndex() == 1) {
                      cbFilter.setItems(filter1);
                      cbFilter.getSelectionModel().selectFirst();
                  } else if (cbSearch.getSelectionModel().getSelectedIndex() == 2) {
                      cbFilter.setItems(filter2);
                      cbFilter.getSelectionModel().selectFirst();
                  }
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
        buildData(1, tableView, Assignment2DAO.getStmt(index));
    }

    @FXML
    private void getResult() throws SQLException, ClassNotFoundException {
        String stmt = "";
        int as = 0;
        if(cbSearch.getSelectionModel().getSelectedIndex() == 0) {
            stmt = "select * from student";
        } else if(cbSearch.getSelectionModel().getSelectedIndex() == 1 && cbFilter.getSelectionModel().getSelectedIndex() == 0) {
            stmt = "select c.courseCode, c.credits,\n" +
                    "(select count(*) from hasStudied hs where hs.courseCode = c.courseCode and grade = 'A') * 100 /\n" +
                    "(select count(*) from hasStudied hs1 where hs1.courseCode = c.courseCode) as '% of A''s'\n" +
                    "from course c\n" +
                    "join hasStudied hs3 on c.courseCode = hs3.courseCode\n" +
                    "group by c.courseCode, c.credits";
        } else if(cbSearch.getSelectionModel().getSelectedIndex() == 1 && cbFilter.getSelectionModel().getSelectedIndex() == 1) {
            stmt = "select c.courseCode, \n" +
                    "(select count(*) from hasStudied hs where hs.courseCode = c.courseCode and grade != 'F') * 100 /\n" +
                    "(select count(*) from hasStudied hs1 where hs1.courseCode = c.courseCode) as 'Troughput'\n" +
                    "from course c\n" +
                    "join hasStudied hs3 on c.courseCode = hs3.courseCode\n" +
                    "group by c.courseCode\n" +
                    "order by Troughput DESC";
        } else if(cbSearch.getSelectionModel().getSelectedIndex() == 2 && cbFilter.getSelectionModel().getSelectedIndex() == 0) {
            stmt = "select * from studies";
        } else if(cbSearch.getSelectionModel().getSelectedIndex() == 2 && cbFilter.getSelectionModel().getSelectedIndex() == 1) {
            stmt = "select * from hasStudied";
        }

        tvOverview.getColumns().clear();
        buildData(0, tvOverview, stmt);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
        	
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
        } catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();        	
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
                        buildData(0, tvRegistration, buildStatement(2));
                    } else {
		                lblMessage.setText("Message: Student has already completed this course.");
                    }
		        } else {
	            lblMessage.setText("Message: Student is studying too many courses");
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
                    StudiesDAO.removeStudies(Integer.parseInt(cbRegStudents.getSelectionModel().getSelectedItem()), Integer.parseInt(cbRegCourses.getSelectionModel().getSelectedItem()));
                    lblMessage.setText("Message: Removed student " + getID(cbRegStudents) + " from course " + getID(cbRegCourses));
                    resetFields();
                    buildData(0, tvRegistration, buildStatement(2));
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
                buildData(0,tvRegistration,buildStatement(2));
                
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
    
    public String buildStatement(int index) {
        String stmt = "";
        switch (index) {
            case 0:
                stmt = StudiesDAO.getAllUnfinishedCourseStmt()+searchCourseGetID()+"";
                break;
            case 1:
                stmt = HasStudiedDAO.getAllCompletedCourseStmt()+searchCourseGetID()+"";
                break;
            case 2:
                stmt = Assignment2DAO.getRegistrations();
                break;
        }
        return stmt;
    }

    
    private int searchCourseGetID() {
		String cID = tfSearch.getText().toString();
    	if (cbSearch.getSelectionModel().getSelectedItem().equals("Course") && !cID.equals("")) {
    		String valueID = cID.substring(1);
    		int courseCode= Integer.parseInt(valueID);
    		return courseCode;
    	} else {
    		lblError.setText("Please enter a valid courseCode.");
    		return 0;
    	}

    }
}
