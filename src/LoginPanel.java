import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private MainApp mainApp;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    public LoginPanel(MainApp mainApp) {
        this.mainApp = mainApp;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameLabel.setForeground(new Color(33, 37, 41)); // Dark gray

        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBackground(new Color(248, 249, 250)); // Light gray
        usernameField.setForeground(new Color(33, 37, 41));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setForeground(new Color(33, 37, 41));

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBackground(new Color(248, 249, 250));
        passwordField.setForeground(new Color(33, 37, 41));

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 123, 255)); // Bootstrap primary blue
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        JButton registerButton = new JButton("Register");
        registerButton.setBackground(new Color(108, 117, 125)); // Bootstrap secondary gray
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        messageLabel.setForeground(new Color(220, 53, 69)); // Bootstrap danger red

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST; add(usernameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST; add(usernameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST; add(passwordLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST; add(passwordField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.CENTER; add(loginButton, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.CENTER; add(registerButton, gbc);
        gbc.gridwidth = 2; gbc.gridx = 0; gbc.gridy = 3; add(messageLabel, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String hashedPassword = PasswordUtil.hashPassword(password);
                System.out.println("Login - Hashed Password: " + hashedPassword);

                try (java.sql.Connection conn = DatabaseUtil.getConnection();
                     java.sql.PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
                    stmt.setString(1, username);
                    stmt.setString(2, hashedPassword);
                    java.sql.ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        int id = rs.getInt("id");
                        String fullName = rs.getString("full_name");
                        String role = rs.getString("role");
                        int departmentId = rs.getInt("department_id");
                        User user = new User(id, username, fullName, role);
                        user.setDepartmentId(departmentId);
                        mainApp.showDashboard(user);
                    } else {
                        messageLabel.setText("Invalid username or password.");
                    }
                } catch (java.sql.SQLException ex) {
                    ex.printStackTrace();
                    System.err.println("Login database error: " + ex.getMessage());
                    messageLabel.setText("Database error: " + ex.getMessage());
                }
            }
        });

        registerButton.addActionListener(e -> mainApp.showRegistration());
    }
}
