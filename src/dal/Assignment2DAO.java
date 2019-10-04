package dal;

import java.sql.SQLException;

public class Assignment2DAO {

	 public static String getStmt(int index) {
		 String stmt = "";
		 switch (index) {
			 case 0: stmt = "select No_, [First Name], [Last Name], Address, City\n" +
					 "from [CRONUS Sverige AB$Employee]\n";
			 break;
			 case 1: stmt = "select [Employee No_], [From Date], [To Date], Description, Quantity\n" +
					 "from [CRONUS Sverige AB$Employee Absence]\n";
			 break;
			 case 2: stmt = "select timestamp, [Search Limit], [Temp_ Key Index], [Temp_ Table No_], [Temp_ Option Value]\n" +
					 "from [CRONUS Sverige AB$Employee Portal Setup]\n";
			 break;
			 case 3: stmt = "select [Employee No_], [Qualification Code], Description, Institution_Company, Cost\n" +
					 "from [CRONUS Sverige AB$Employee Qualification]\n";
			 break;
			 case 4: stmt = "select [Employee No_], [Relative Code], [First Name], [Last Name], [Birth Date]\n" +
					 "from [CRONUS Sverige AB$Employee Relative]\n";
			 break;
			 case 5: stmt = "select *\n" +
					 "from [CRONUS Sverige AB$Employee Statistics Group]\n";
			 break;
			 case 6: stmt = "select *\n" +
					 "from [CRONUS Sverige AB$Employment Contract]\n";
			 break;
			 case 7: stmt = "select name, type from sys.key_constraints;";
			 break;
			 case 8: stmt = "select constraint_name, constraint_type\n" +
			 "from information_schema.table_constraints";
			 break;
			 case 9: stmt = "select table name\n" +
					 "from information_schema.tables\n";
			 break;
			 case 10: stmt = "select column_name\n" +
					 "from information_schema.columns\n" +
					 "where table_name ='CRONUS Sverige AB$Employee'\n";
			 break;
			 case 11: stmt = "select table_name, column_name\n" +
					 "from information_schema.columns\n" +
					 "where table_name like '%Employee%'";
			 break;
			 case 12: stmt = "select top 1\n" +
					 "    [TableName] = o.name, \n" +
					 "    [RowCount] = i.rows\n" +
					 "from \n" +
					 "    sysobjects o, \n" +
					 "    sysindexes i \n" +
					 "where \n" +
					 "    o.xtype = 'U' \n" +
					 "    and \n" +
					 "    i.id = object_id(o.name)\n" +
					 "order by i.rows desc\n";
			 break;
			 case 13: stmt = "select [Relational Exch_ Rate Amount]\n" +
					 "from [CRONUS Sverige AB$Currency Exchange Rate]\n" +
					 "where [Currency Code] = 'NOK'\n";
			 break;
			 case 14: stmt = "select [Currency Code]\n" +
					 "from [CRONUS Sverige AB$Currency Exchange Rate]\n" +
					 "where ([Relational Exch_ Rate Amount] = (select max([Relational Exch_ Rate Amount])\n" +
					 "from [CRONUS Sverige AB$Currency Exchange Rate]))";
			 break;
			 case 15: stmt = "select Address, City\n" +
					 "from [CRONUS Sverige AB$Customer]\n" +
					 "where [Search Name] like '%FOTOGRAFERNA%'\n";
			 break;
			 case 16: stmt = "select distinct emp.[Search Name]\n" +
					 "from [CRONUS Sverige AB$Employee] emp\n" +
					 "join [CRONUS Sverige AB$Employee Absence] empAb\n" +
					 "on emp.No_ = empAb.[Employee No_]\n" +
					 "where empAb.[Cause of Absence Code] = 'SJUK'\n";
			 break;
			 case 17: stmt = "select emp.[Search Name], empRel.[First Name], empRel.[Last Name], empRel.[Relative Code]\n" +
					 "from [CRONUS Sverige AB$Employee Relative] empRel\n" +
					 "join [CRONUS Sverige AB$Employee] emp\n" +
					 "on empRel.[Employee No_] = emp.No_\n" +
					 "order by emp.[Search Name]\n";
			 break;
			 case 18: stmt = "select cus.Name\n" +
					 "from [CRONUS Sverige AB$Customer] cus\n" +
					 "join [CRONUS Sverige AB$Employee] emp\n" +
					 "on cus.[Salesperson Code] = emp.No_\n" +
					 "where emp.[First Name] = 'Andreas' and emp.[Last Name] = 'Berglund'\n";
			 break;
			 case 19: stmt = "select [Bank Account No_]\n" +
					 "from [CRONUS Sverige AB$Customer Bank Account] \n" +
					 "where [CRONUS Sverige AB$Customer Bank Account].[Customer No_] = '10000'\n";
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
	 
	 public static String getRegistrationsForCourse(int ID) {
		 String stmt = "select studentID, courseCode, '' as grade\n" +
					"from studies\n" +
					"union\n" +
					"where courseCode="+ID+""+
					"select *\n" +
					"from HasStudied\n"+
					"where courseCode ="+ID+"";
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
	 
	
		 
}
