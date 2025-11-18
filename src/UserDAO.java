import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * UserDAO (Data Access Object)
 * Yeh class database se login check karti hai.
 */
public class UserDAO {

    /**
     * Username aur Password ko validate karta hai.
     * @return LoggedInUser object agar successful, ya null agar fail.
     */
    public LoggedInUser validateUser(String username, String password) {

        // 1. Password ko Hash karo
        String hashedPassword = PasswordUtil.hashPassword(password);

        // --- DEBUGGING START (Yeh console mein hash dikhayega) ---
        System.out.println("\n----------------------------------------");
        System.out.println("DEBUGGING LOGIN:");
        System.out.println("Input Username: " + username);
        System.out.println("Input Password: " + password);
        System.out.println("Generated Hash: " + hashedPassword);
        System.out.println("----------------------------------------\n");
        // --- DEBUGGING END ---

        if (hashedPassword == null) {
            return null; // Hashing mein error
        }

        // 2. Database se check karo
        String query = "SELECT role, linked_emp_id FROM Users WHERE username = ? AND password = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Login Successful
                String role = rs.getString("role");
                int empId = rs.getInt("linked_emp_id");

                System.out.println("Database Match Found! Role: " + role); // Ek aur debug message

                return new LoggedInUser(username, role, empId);
            } else {
                System.err.println("Database Match NOT Found!"); // Error message
                return null; // Login Failed
            }

        } catch (SQLException e) {
            System.err.println("Login SQL Error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}