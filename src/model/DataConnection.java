package model;

<<<<<<< HEAD


=======
import java.io.FileInputStream;
>>>>>>> 25e0c8b204172c64228809f9af30a24db05b34e2
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
<<<<<<< HEAD

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
    
=======
import java.util.Properties;

public class DataConnection {
    private static Connection connection;

    public static Connection getConnection() throws SQLException, IOException {
        if (connection == null || connection.isClosed()) {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("src/main/resources/database.properties");
            props.load(fis);
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }
>>>>>>> 25e0c8b204172c64228809f9af30a24db05b34e2

    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }


}