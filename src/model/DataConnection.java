package model;



import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataConnection {
	private static final String URL = "jdbc:mysql://localhost:3306/task_manager2";
	private static final String USER ="root";
	private static final String PASSWORD = "";
	private static Connection connection;

    public static Connection getConnection() throws SQLException, IOException {
    	Connection conn = null;
    	try {
    	Class.forName("com.mysql.cj.jdbc.Driver");
    	
    	
    	 conn = DriverManager.getConnection(URL,USER,PASSWORD);
    	
 //      
    }catch(ClassNotFoundException e) {
    	System.out.println("Driver JDBC non trouve");
    	e.printStackTrace();
    }
    	return conn;
    	}
    

    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }


}