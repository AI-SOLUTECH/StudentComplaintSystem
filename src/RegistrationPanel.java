import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrationPanel extends JPanel {
    private MainApp mainApp;
    private JTextField usernameField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JLabel messageLabel;

    public RegistrationPanel(MainApp mainApp) {
        this.mainApp = mainApp;
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 245, 245)); // Light gray background
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameLabel.setForeground(new Color(33, 37, 41));
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(108, 117, 125), 1));
        usernameField.setBackground(Color.WHITE);

        JLabel fullNameLabel = new JLabel("Full Name:");
        fullNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        fullNameLabel.setForeground(new Color(33, 37, 41));
        fullNameField = new JTextField(20);
        fullNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        fullNameField.setBorder(BorderFactory.createLineBorder(new Color(108, 117, 125), 1));
        fullNameField.setBackground(Color.WHITE);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabel.setForeground(new Color(33, 37, 41));
        emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setBorder(BorderFactory.createLineBorder(new Color(108, 117, 125), 1));
        emailField.setBackground(Color.WHITE);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setForeground(new Color(33, 37, 41));
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(108, 117, 125), 1));
        passwordField.setBackground(Color.WHITE);

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        roleLabel.setForeground(new Color(33, 37, 41));
        String[] roles = {"Student", "Admin Officer", "Department Officer", "System Admin"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        roleComboBox.setBackground(Color.WHITE);
        roleComboBox.setBorder(BorderFactory.createLineBorder(new Color(108, 117, 125), 1));

        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(0, 123, 255));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton backButton = new JButton("Back to Login");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(108, 117, 125));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        messageLabel.setForeground(new Color(220, 53, 69));

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.LINE_END; add(usernameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.LINE_START; add(usernameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.LINE_END; add(fullNameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.LINE_START; add(fullNameField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.LINE_END; add(emailLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.LINE_START; add(emailField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.LINE_END; add(passwordLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.anchor = GridBagConstraints.LINE_START; add(passwordField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.LINE_END; add(roleLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.anchor = GridBagConstraints.LINE_START; add(roleComboBox, gbc);
        gbc.gridx = 0; gbc.gridy = 5; gbc.anchor = GridBagConstraints.CENTER; add(registerButton, gbc);
        gbc.gridx = 1; gbc.gridy = 5; gbc.anchor = GridBagConstraints.CENTER; add(backButton, gbc);
        gbc.gridwidth = 2; gbc.gridx = 0; gbc.gridy = 6; add(messageLabel, gbc);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String fullName = fullNameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String role = (String) roleComboBox.getSelectedItem();

                if (username.isEmpty() || fullName.isEmpty() || password.isEmpty()) {
                    messageLabel.setText("Please fill in all required fields.");
                    return;
                }

                try (java.sql.Connection conn = DatabaseUtil.getConnection();
                     java.sql.PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
                    checkStmt.setString(1, username);
                    java.sql.ResultSet rs = checkStmt.executeQuery();
                    if (rs.next()) {
                        messageLabel.setText("Username already exists.");
                        return;
                    }
                } catch (java.sql.SQLException ex) {
                    ex.printStackTrace();
                    System.err.println("Registration database error: " + ex.getMessage());
                    messageLabel.setText("Database error: " + ex.getMessage());
                    return;
                }

                try (java.sql.Connection conn = DatabaseUtil.getConnection();
                     java.sql.PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO users (username, full_name, email, password, role) VALUES (?, ?, ?, ?, ?)")) {
                    insertStmt.setString(1, username);
                    insertStmt.setString(2, fullName);
                    insertStmt.setString(3, email);
                    String hashedPassword = PasswordUtil.hashPassword(password);
                    System.out.println("Registration - Hashed Password: " + hashedPassword);
                    insertStmt.setString(4, hashedPassword);
                    insertStmt.setString(5, role);
                    int rows = insertStmt.executeUpdate();
                    if (rows > 0) {
                        messageLabel.setText("Registration successful! Please login.");
                    } else {
                        messageLabel.setText("Registration failed. Please try again.");
                    }
                } catch (java.sql.SQLException ex) {
                    ex.printStackTrace();
                    System.err.println("Registration insert error: " + ex.getMessage());
                    messageLabel.setText("Registration failed: " + ex.getMessage());
                }
            }
        });

        backButton.addActionListener(e -> mainApp.showLogin());
    }
}
