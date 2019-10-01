package dal;

public class Assignment2DAO {
	 public static String getStmt(int index) {
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

}