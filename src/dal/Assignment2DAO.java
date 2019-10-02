package dal;

import java.sql.SQLException;

public class Assignment2DAO {

	 public static String getStmt(int index) {
		 String stmt = "";
		 switch (index) {
			 case 0: stmt = "SELECT No_, [First Name], [Last Name], Address, City\n" +
					 "FROM [CRONUS Sverige AB$Employee]\n";
			 break;
			 case 1: stmt = "SELECT [Employee No_], [From Date], [To Date], Description, Quantity\n" +
					 "FROM [CRONUS Sverige AB$Employee Absence]\n";
			 break;
			 case 2: stmt = "SELECT timestamp, [Search Limit], [Temp_ Key Index], [Temp_ Table No_], [Temp_ Option Value]\n" +
					 "FROM [CRONUS Sverige AB$Employee Portal Setup]\n";
			 break;
			 case 3: stmt = "SELECT [Employee No_], [Qualification Code], Description, Institution_Company, Cost\n" +
					 "FROM [CRONUS Sverige AB$Employee Qualification]\n";
			 break;
			 case 4: stmt = "SELECT [Employee No_], [Relative Code], [First Name], [Last Name], [Birth Date]\n" +
					 "FROM [CRONUS Sverige AB$Employee Relative]\n";
			 break;
			 case 5: stmt = "SELECT *\n" +
					 "FROM [CRONUS Sverige AB$Employee Statistics Group]\n";
			 break;
			 case 6: stmt = "SELECT *\n" +
					 "FROM [CRONUS Sverige AB$Employment Contract]\n";
			 break;
			 case 7: stmt = "SELECT name, type FROM sys.key_constraints;";
			 break;
			 case 8: stmt = "SELECT CONSTRAINT_NAME, CONSTRAINT_TYPE\n" +
					 "FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS";
			 break;
			 case 9: stmt = "SELECT TABLE_NAME\n" +
					 "FROM INFORMATION_SCHEMA.TABLES\n";
			 break;
			 case 10: stmt = "SELECT COLUMN_NAME\n" +
					 "FROM INFORMATION_SCHEMA.COLUMNS\n" +
					 "WHERE TABLE_NAME = 'CRONUS Sverige AB$Employee'\n";
			 break;
			 case 11: stmt = "SELECT TABLE_NAME, COLUMN_NAME\n" +
					 "FROM INFORMATION_SCHEMA.COLUMNS\n" +
					 "WHERE TABLE_NAME LIKE '%Employee%'";
			 break;
			 case 12: stmt = "SELECT TOP 1\n" +
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
			 break;
		 }
		 return stmt;
	 }

	 public static String getRegistrations() {
	 	String stmt = "select studentID, courseCode, '' as grade\n" +
				"from studies\n" +
				"union\n" +
				"select *\n" +
				"from HasStudied";
	 	return stmt;
	 }
	 
	 public static String getPercentAStmt() {
		 String stmt = "select c.courseCode, c.credits,\n" +
	             "(select count(*) from hasStudied hs where hs.courseCode = c.courseCode and grade = 'A') * 100 /\n" +
	             "(select count(*) from hasStudied hs1 where hs1.courseCode = c.courseCode) as '% of A''s'\n" +
	             "from course c\n" +
	             "join hasStudied hs3 on c.courseCode = hs3.courseCode\n" +
	             "group by c.courseCode, c.credits";
		 return stmt;
	 }
	 
	 public static String getThroughputStmt() {
		 String stmt = "select c.courseCode, \n" +
                 "(select count(*) from hasStudied hs where hs.courseCode = c.courseCode and grade != 'F') * 100 /\n" +
                 "(select count(*) from hasStudied hs1 where hs1.courseCode = c.courseCode) as 'Troughput'\n" +
                 "from course c\n" +
                 "join hasStudied hs3 on c.courseCode = hs3.courseCode\n" +
                 "group by c.courseCode\n" +
                 "order by Troughput DESC";
		 return stmt;
	 }
	 
	 public static String getAllStudents() {
		 String stmt = "select * from Student";
		 return stmt;
	 }
		 
}
