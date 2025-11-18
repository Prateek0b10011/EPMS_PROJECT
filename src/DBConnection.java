import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Yeh details humne MySQL install karte waqt set ki thi
    // Database ka naam 'epms_db' hai
    private static final String URL = "jdbc:mysql://localhost:3306/epms_db";
    private static final String USER = "root"; // Aapka MySQL username

    // !! IMPORTANT: Yahan apna password daaliye !!
    private static final String PASSWORD = "physcoviper@1234";

    // Method jo connection establish karega
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // 1. Load the Driver (Yeh .jar file ko load karta hai)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. Establish the Connection
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            if (connection != null) {
                System.out.println("Connection successful!");
            }

        } catch (SQLException e) {
            System.err.println("Database connection FAILED!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found! (Driver add nahi hua)");
            e.printStackTrace();
        }
        return connection;
    }

    // Ek main method sirf connection test karne ke liye
    public static void main(String[] args) {
        // Yeh method call karke check karega ki connection ho raha hai ya nahi
        getConnection();
    }
}