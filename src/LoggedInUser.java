/**
 * Ek simple class jo login karne wale user ki details ko hold karti hai.
 */
public class LoggedInUser {
    private final String username;
    private final String
            role; // 'ADMIN' ya 'EMPLOYEE'
    private final int linkedEmpId; // 0 agar admin, ya employee ki ID

    // Constructor
    public LoggedInUser(String username, String role, int linkedEmpId) {
        this.username = username;
        this.role = role;
        this.linkedEmpId = linkedEmpId;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public int getLinkedEmpId() {
        return linkedEmpId;
    }

    // Helper method
    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(this.role);
    }
}