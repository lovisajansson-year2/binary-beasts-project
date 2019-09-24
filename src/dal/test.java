package dal;

import java.sql.ResultSet;
import java.sql.SQLException;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {			
			ResultSet rs = CourseDAO.findCourse("c2");
			while (rs.next()) {
				System.out.println(rs.getString(1));
			}
					
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
