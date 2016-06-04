/**
 * @author Bijon
 *
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreateConnectionMySQL {
	
	private Connection connect = null;
	
	/**
	 * Creates connection object to connect to database
	 * @return connecttion object connect
	 */
	public Connection createConn(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://localhost/url?user=bijon&password=bijon");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return connect;
		
	}
}
