package controllers;

import dal.*;

import database.DatabaseConnection;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.util.Callback;
import models.Course;
import models.Student;
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

    //Overview
    @FXML
    private TextField tfSearch;
    @FXML
    private ComboBox<String> cbSearch;
    @FXML
    private ComboBox<String> cbFilter;
    @FXML
    private TableView tbOverview;
    @FXML
    private TextArea taGrades;


    @FXML
    private TableView tableView;
    @FXML
    private ComboBox<String> cbQ;
    @FXML
    private Button btnA1;

    private void resetFields() {
      	 cbRegStudents.getSelectionModel().selectFirst();
   	 cbRegCourses.getSelectionModel().selectFirst();
   	 cbGrade.getSelectionModel().selectFirst();
   	 cbStudent.getSelectionModel().selectFirst();
   	 cbCourses.getSelectionModel().selectFirst();
   	 tfFirstName.setText(null);
   	 tfLastName.setText(null);

       } 
    private static String getItem(ComboBox<String> comboBoxName ) {
       	String item = comboBoxName.getSelectionModel().getSelectedItem();
       	return item;
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

        //Overview
        ObservableList<String> search = FXCollections.observableArrayList("Student", "Course", "Relation");
        cbSearch.setItems(search);
        cbSearch.getSelectionModel().selectFirst();

        ObservableList<String> questions = FXCollections.observableArrayList("0.1","0.2","0.3","0.4","0.5","0.6","0.7",
        "1.1","1.2","1.3","1.4","1.5","1.6","2.1","2.2","2.3","2.4","2.5","2.6","2.7");

        cbQ.setItems(questions);
        cbQ.getSelectionModel().selectFirst();

        listenerStudent();
        listenerCourse();
        listenerOverview();
    }

    private String getStmt(int index) {
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
            stmt = "SELECT TABLE_NAME, COLUMN_NAME\n" +
                    "FROM INFORMATION_SCHEMA.COLUMNS\n" +
                    "WHERE TABLE_NAME LIKE '%Employee%'";
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
        return stmt;

    }

    private void buildData(int connection, TableView tableView, String stmt) throws SQLException, ClassNotFoundException {
        ObservableList data = FXCollections.observableArrayList();

        try {
            ResultSet rs = DatabaseConnection.dbExecuteQuery(connection, stmt);

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
            throw e;
        }

    }

    @FXML
    public void onEnter(ActionEvent actionEvent) throws SQLException, ClassNotFoundException{
        getResult();
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
                ObservableList<String> filter1 = FXCollections.observableArrayList("None", "Troughput");
                ObservableList<String> filter2 = FXCollections.observableArrayList("Started", "Finished");
                try {
                  if (cbSearch.getSelectionModel().getSelectedIndex() == 0) {
                        cbFilter.setItems(null);
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
        buildData(1, tableView, getStmt(index));
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

        tbOverview.getColumns().clear();
        buildData(0, tbOverview, stmt);
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
        	StudentDAO.updateStudent(Integer.parseInt(cbStudent.getSelectionModel().getSelectedItem()), tfFirstName.getText(), tfLastName.getText());
            lblMessage.setText("Message: Student " + getItem(cbStudent)+" updated.");
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
        		StudentDAO.removeStudent(Integer.parseInt(cbStudent.getSelectionModel().getSelectedItem()));
        		lblMessage.setText("Message: Removed student " + getItem(cbStudent));
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
            CourseDAO.updateCourse(Integer.parseInt(cbCourses.getSelectionModel().getSelectedItem()), Integer.parseInt(tfCredits.getText()));
            lblMessage.setText("Message: Course "+getItem(cbCourses)+" Updated.");
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
            CourseDAO.removeCourse(Integer.parseInt(cbCourses.getSelectionModel().getSelectedItem()));
            lblMessage.setText("Message: Removed course " + getItem(cbCourses));
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
    		if(index!=0 && index2!=0) {
		        int sID = Integer.parseInt(getItem(cbRegStudents));
		        int credits = 0;
		        for(Course c : StudiesDAO.findAllStudiesForStudents(sID)) {
		        	credits = credits + c.getCredits();
		        }if(credits <= 45) {
		            StudiesDAO.addStudies(sID, Integer.parseInt(cbRegCourses.getSelectionModel().getSelectedItem()));
		            lblMessage.setText("Message: Registered " +getItem(cbRegStudents)+" on course "+getItem(cbRegCourses));
		            resetFields();
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
         	if(index!=0 && index2!=0) {	
				StudiesDAO.removeStudies(Integer.parseInt(cbRegStudents.getSelectionModel().getSelectedItem()), Integer.parseInt(cbRegCourses.getSelectionModel().getSelectedItem()));
         		lblMessage.setText("Message: Removed student " + getItem(cbRegStudents)+" from course " +getItem(cbRegCourses));
         		resetFields();
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
        	if(index!=0 && index2!=0) {
				HasStudiedDAO.addHasStudied(Integer.parseInt(cbRegStudents.getSelectionModel().getSelectedItem()), Integer.parseInt(cbRegCourses.getSelectionModel().getSelectedItem()), cbGrade.getSelectionModel().getSelectedItem());			
        		lblMessage.setText("Message: set Grade " + getItem(cbGrade)+" for Student "+getItem(cbRegStudents)+" on course " + getItem(cbRegCourses) );
        		resetFields();
        	}else if(index+index2==0) {
        		lblMessage.setText("Message: You have to pick a student and a course to update grade");
        	} else if(index==0) {
        		lblMessage.setText("Message: You have to pick a student to update grade");
        	} else if(index2==0) {
        		lblMessage.setText("Message: You have to pick a course to update grade.");
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
}
