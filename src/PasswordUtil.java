import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Ek helper class jo password ko secure SHA-256 hash mein convert karti hai.
 */
public class PasswordUtil {

    /**
     * Ek plain text password leta hai aur uska SHA-256 hash return karta hai.
     */
    public static String hashPassword(String password) {
        try {
            // SHA-256 algorithm ka instance create karo
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Password ko bytes mein convert karke hash generate karo
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

            // Hashed bytes ko hexadecimal string mein convert karo (taaki database mein save kar sakein)
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            // Yeh error tab aata hai agar SHA-256 algorithm exist na kare (jo impossible hai)
            System.err.println("SHA-256 Algorithm not found!");
            e.printStackTrace();
            return null; // Error case
        }
    }

    // --- Ek chota test (check karne ke liye ki yeh kaam kar raha hai) ---
    public static void main(String[] args) {
        String pass = "priya123";
        String hash = hashPassword(pass);

        System.out.println("Original Password: " + pass);
        System.out.println("Hashed Password: " + hash);
        // Hashed password hamesha 'ef92b778bafe771e89245b89ecbc08a44a4e5d6d' (example) jaisa dikhega
    }
}