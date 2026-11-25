import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Connection settings are kept in one separate class
public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/university_db";
    private static final String USER = "root";
    private static final String PASSWORD = "your_password";   // put your MySQL password here

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
