package dal;

import database.DatabaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Assignment2DAO {

	 public static ResultSet getStmt(int index) throws SQLException, ClassNotFoundException {
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
			 case 8: stmt = "select CONSTRAINT_NAME, CONSTRAINT_TYPE\n" +
			 "from information_schema.table_constraints";
			 break;
			 case 9: stmt = "select TABLE_NAME\n" +
					 "from information_schema.tables\n";
			 break;
			 case 10: stmt = "select COLUMN_NAME\n" +
					 "from information_schema.columns\n" +
					 "where TABLE_NAME ='CRONUS Sverige AB$Employee'\n";
			 break;
			 case 11: stmt = "select TABLE_NAME, COLUMN_NAME\n" +
					 "from information_schema.columns\n" +
					 "where TABLE_NAME like '%Employee%'";
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
		 ResultSet rs = DatabaseConnection.dbExecuteQuery(1, stmt);
		 return rs;
	 }

	 public static ResultSet getRegistrations() throws SQLException, ClassNotFoundException {
	 	String stmt = "select studentID, courseCode, '' as grade\n" +
				"from studies\n" +
				"union\n" +
				"select *\n" +
				"from HasStudied";
		 ResultSet rs = DatabaseConnection.dbExecuteQuery(0, stmt);
	 	return rs;
	 }
}
