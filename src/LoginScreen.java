import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JFrame {

    private JTextField userText;
    private JPasswordField passText;
    private JButton loginButton;

    // Backend Connection
    private UserDAO userDAO = new UserDAO();

    public LoginScreen() {
        // 1. Window Setup
        setTitle("EPMS Login - Secure Access");
        setSize(400, 250); // Window ka size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Screen ke beech mein open hoga
        setResizable(false);

        // 2. Layout Design (Panel banana)
        JPanel panel = new JPanel();
        panel.setLayout(null); // Hum khud coordinates set karenge
        panel.setBackground(new Color(240, 248, 255)); // Halka neela rang
        add(panel);

        // --- Heading ---
        JLabel titleLabel = new JLabel("EPMS Login");
        titleLabel.setBounds(140, 10, 150, 30);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(25, 25, 112));
        panel.add(titleLabel);

        // --- Username ---
        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 60, 80, 25);
        panel.add(userLabel);

        userText = new JTextField(20);
        userText.setBounds(140, 60, 180, 25);
        panel.add(userText);

        // --- Password ---
        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 100, 80, 25);
        panel.add(passLabel);

        passText = new JPasswordField(20);
        passText.setBounds(140, 100, 180, 25);
        panel.add(passText);

        // --- Login Button ---
        loginButton = new JButton("Login");
        loginButton.setBounds(140, 150, 100, 30);
        loginButton.setBackground(new Color(34, 139, 34)); // Green color
        loginButton.setForeground(Color.WHITE);
        panel.add(loginButton);

        // 3. Button Action (Jab button dabega tab kya hoga)
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        setVisible(true); // Window ko dikhao
    } // <- Constructor yahan khatam hota hai

    /**
     * Login logic ko handle karta hai
     */
    private void performLogin() {
        String username = userText.getText();
        String password = new String(passText.getPassword());

        // Backend se check karo
        LoggedInUser user = userDAO.validateUser(username, password);

        if (user != null) {
            // Login Successful
            dispose(); // Login window band karo

            // Naya Dashboard (MainApplication) kholo
            // (MainApplication apne constructor mein setVisible(true) call karega)
            new MainApplication(user);

        } else {
            // Login Failed
            JOptionPane.showMessageDialog(this, "Invalid Username or Password!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } // <- performLogin method yahan khatam hota hai

    /**
     * Application ko start karne ke liye Main method
     */
    public static void main(String[] args) {
        // Best practice ke liye Swing ko EDT par run karo
        SwingUtilities.invokeLater(() -> new LoginScreen());
    }

}