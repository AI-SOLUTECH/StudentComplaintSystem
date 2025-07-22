import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class DashboardPanel extends JPanel {
    private MainApp mainApp;
    private User user;
    private JLabel welcomeLabel;
    private JButton logoutButton;

    private JPanel contentPanel;

    public DashboardPanel(MainApp mainApp) {
        this.mainApp = mainApp;
        setLayout(new BorderLayout());

        welcomeLabel = new JLabel();
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setForeground(new Color(0, 123, 255)); // Bootstrap primary blue

        logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(220, 53, 69)); // Bootstrap danger red
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(new Color(248, 249, 250)); // Light gray background
        topPanel.add(welcomeLabel);
        topPanel.add(logoutButton);

        add(topPanel, BorderLayout.NORTH);

        contentPanel = new JPanel(new CardLayout());
        add(contentPanel, BorderLayout.CENTER);

        logoutButton.addActionListener(e -> mainApp.showLogin());
    }

    public void setUser(User user) {
        this.user = user;
        welcomeLabel.setText("Welcome, " + user.getFullName() + " (" + user.getRole() + ")");
        showDashboardForRole(user.getRole());
    }

    private void showDashboardForRole(String role) {
        contentPanel.removeAll();
        switch (role) {
            case "Student":
                showStudentDashboard();
                break;
            case "System Admin":
                showSystemAdminDashboard();
                break;
            case "Admin Officer":
                showAdminOfficerDashboard();
                break;
            case "Department Officer":
                showDepartmentOfficerDashboard();
                break;
            default:
                contentPanel.add(new JLabel("Unknown role: " + role));
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showStudentDashboard() {
        JPanel panel = new JPanel(new BorderLayout());

        // Complaint submission form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Submit New Complaint"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel("Title:");
        JTextField titleField = new JTextField(20);
        JLabel descriptionLabel = new JLabel("Description:");
        JTextArea descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        
        JLabel departmentLabel = new JLabel("Department:");
        JComboBox<String> departmentComboBox = new JComboBox<>(new String[]{"IT", "Academic", "Finance", "Administration"});
        
        JButton submitButton = new JButton("Submit Complaint");
        submitButton.setBackground(new Color(0, 123, 255));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(titleLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(titleField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.NORTHWEST; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(descriptionLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(descriptionScrollPane, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(departmentLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(departmentComboBox, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(submitButton, gbc);

        // My complaints list
        JPanel complaintsPanel = new JPanel(new BorderLayout());
        complaintsPanel.setBorder(BorderFactory.createTitledBorder("My Complaints"));

        DefaultListModel<Complaint> complaintListModel = new DefaultListModel<>();
        JList<Complaint> complaintList = new JList<>(complaintListModel);
        complaintList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane complaintListScrollPane = new JScrollPane(complaintList);

        JTextArea complaintDescriptionArea = new JTextArea();
        complaintDescriptionArea.setEditable(false);
        complaintDescriptionArea.setLineWrap(true);
        complaintDescriptionArea.setWrapStyleWord(true);
        complaintDescriptionArea.setBorder(BorderFactory.createTitledBorder("Description"));

        complaintsPanel.add(complaintListScrollPane, BorderLayout.CENTER);
        complaintsPanel.add(new JScrollPane(complaintDescriptionArea), BorderLayout.SOUTH);

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(complaintsPanel, BorderLayout.CENTER);

        // Load user's complaints
        try {
            List<Complaint> complaints = ComplaintManager.getComplaintsByUser(user.getId());
            for (Complaint c : complaints) {
                complaintListModel.addElement(c);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading complaints: " + e.getMessage());
        }

        // Display complaint title and status in list
        complaintList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Complaint) {
                    Complaint complaint = (Complaint) value;
                    setText(complaint.getId() + ": " + complaint.getTitle() + " - " + complaint.getStatus());
                }
                return this;
            }
        });

        // Update description area on selection
        complaintList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Complaint selected = complaintList.getSelectedValue();
                if (selected != null) {
                    complaintDescriptionArea.setText(selected.getDescription());
                } else {
                    complaintDescriptionArea.setText("");
                }
            }
        });

        // Submit complaint action
        submitButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String description = descriptionArea.getText().trim();
            String department = (String) departmentComboBox.getSelectedItem();

            if (title.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }

            try {
                // For simplicity, using department index + 1 as department ID
                int departmentId = departmentComboBox.getSelectedIndex() + 1;
                ComplaintManager.addComplaint(user.getId(), departmentId, title, description);
                JOptionPane.showMessageDialog(this, "Complaint submitted successfully.");
                
                // Clear form
                titleField.setText("");
                descriptionArea.setText("");
                departmentComboBox.setSelectedIndex(0);
                
                // Refresh complaints list
                complaintListModel.clear();
                List<Complaint> complaints = ComplaintManager.getComplaintsByUser(user.getId());
                for (Complaint c : complaints) {
                    complaintListModel.addElement(c);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error submitting complaint: " + ex.getMessage());
            }
        });

        contentPanel.add(panel);
    }

    private void showAdminOfficerDashboard() {
        JPanel panel = new JPanel(new BorderLayout());

        // All complaints list for admin officer
        JPanel complaintsPanel = new JPanel(new BorderLayout());
        complaintsPanel.setBorder(BorderFactory.createTitledBorder("All Complaints"));

        DefaultListModel<Complaint> complaintListModel = new DefaultListModel<>();
        JList<Complaint> complaintList = new JList<>(complaintListModel);
        complaintList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane complaintListScrollPane = new JScrollPane(complaintList);

        JTextArea complaintDescriptionArea = new JTextArea();
        complaintDescriptionArea.setEditable(false);
        complaintDescriptionArea.setLineWrap(true);
        complaintDescriptionArea.setWrapStyleWord(true);
        complaintDescriptionArea.setBorder(BorderFactory.createTitledBorder("Description"));

        complaintsPanel.add(complaintListScrollPane, BorderLayout.CENTER);
        complaintsPanel.add(new JScrollPane(complaintDescriptionArea), BorderLayout.SOUTH);

        panel.add(complaintsPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout());

        JButton resolveButton = new JButton("Mark as Resolved");
        JButton escalateButton = new JButton("Escalate Complaint");
        JButton deleteButton = new JButton("Delete Complaint");

        buttonsPanel.add(resolveButton);
        buttonsPanel.add(escalateButton);
        buttonsPanel.add(deleteButton);

        panel.add(buttonsPanel, BorderLayout.SOUTH);

        // Load all complaints
        try {
            List<Complaint> complaints = ComplaintManager.getAllComplaints();
            for (Complaint c : complaints) {
                complaintListModel.addElement(c);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading complaints: " + e.getMessage());
        }

        // Display complaint title, username and status in list
        complaintList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Complaint) {
                    Complaint complaint = (Complaint) value;
                    setText(complaint.getId() + ": " + complaint.getTitle() + " (User: " + complaint.getUsername() + ") - " + complaint.getStatus());
                }
                return this;
            }
        });

        // Update description area on selection
        complaintList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Complaint selected = complaintList.getSelectedValue();
                if (selected != null) {
                    complaintDescriptionArea.setText(selected.getDescription());
                } else {
                    complaintDescriptionArea.setText("");
                }
            }
        });

        // Resolve complaint action
        resolveButton.addActionListener(e -> {
            Complaint selected = complaintList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select a complaint to resolve.");
                return;
            }
            try {
                ComplaintManager.updateComplaintStatus(selected.getId(), "Resolved");
                JOptionPane.showMessageDialog(this, "Complaint marked as resolved.");
                refreshComplaintsList(complaintListModel);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error updating complaint status: " + ex.getMessage());
            }
        });

        // Escalate complaint action
        escalateButton.addActionListener(e -> {
            Complaint selected = complaintList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select a complaint to escalate.");
                return;
            }
            try {
                ComplaintManager.updateComplaintStatus(selected.getId(), "Escalated");
                JOptionPane.showMessageDialog(this, "Complaint escalated.");
                refreshComplaintsList(complaintListModel);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error updating complaint status: " + ex.getMessage());
            }
        });

        // Delete complaint action
        deleteButton.addActionListener(e -> {
            Complaint selected = complaintList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select a complaint to delete.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this complaint?", 
                "Confirm Delete", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    ComplaintManager.deleteComplaint(selected.getId());
                    JOptionPane.showMessageDialog(this, "Complaint deleted successfully.");
                    refreshComplaintsList(complaintListModel);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error deleting complaint: " + ex.getMessage());
                }
            }
        });

        contentPanel.add(panel);
    }

    private void refreshComplaintsList(DefaultListModel<Complaint> model) {
        try {
            model.clear();
            List<Complaint> complaints = ComplaintManager.getAllComplaints();
            for (Complaint c : complaints) {
                model.addElement(c);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error refreshing complaints: " + e.getMessage());
        }
    }

    private void showSystemAdminDashboard() {
        JPanel panel = new JPanel(new BorderLayout());

        // User management panel
        JPanel userManagementPanel = new JPanel(new BorderLayout());
        userManagementPanel.setBorder(BorderFactory.createTitledBorder("User Management"));

        DefaultListModel<String> userListModel = new DefaultListModel<>();
        JList<String> userList = new JList<>(userListModel);
        JScrollPane userListScrollPane = new JScrollPane(userList);

        JPanel userFormPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField usernameField = new JTextField();
        JTextField fullNameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField passwordField = new JTextField();
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"Student", "Admin Officer", "Department Officer", "System Admin"});
        JButton addUserButton = new JButton("Add User");
        JButton deleteUserButton = new JButton("Delete User");
        JButton updateRoleButton = new JButton("Update Role");

        userFormPanel.add(new JLabel("Username:"));
        userFormPanel.add(usernameField);
        userFormPanel.add(new JLabel("Full Name:"));
        userFormPanel.add(fullNameField);
        userFormPanel.add(new JLabel("Email:"));
        userFormPanel.add(emailField);
        userFormPanel.add(new JLabel("Password:"));
        userFormPanel.add(passwordField);
        userFormPanel.add(new JLabel("Role:"));
        userFormPanel.add(roleComboBox);
        userFormPanel.add(addUserButton);
        userFormPanel.add(deleteUserButton);
        userFormPanel.add(updateRoleButton);

        userManagementPanel.add(userListScrollPane, BorderLayout.CENTER);
        userManagementPanel.add(userFormPanel, BorderLayout.SOUTH);

        // Load users
        try {
            java.util.List<User> users = UserManager.getAllUsers();
            for (User u : users) {
                userListModel.addElement(u.getId() + ": " + u.getUsername() + " (" + u.getRole() + ")");
            }
        } catch (java.sql.SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + e.getMessage());
        }

        // Add user action
        addUserButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();
            String role = (String) roleComboBox.getSelectedItem();

            if (username.isEmpty() || fullName.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username, Full Name, and Password are required.");
                return;
            }
            try {
                UserManager.addUser(username, fullName, email, password, role);
                JOptionPane.showMessageDialog(this, "User added successfully.");
                usernameField.setText("");
                fullNameField.setText("");
                emailField.setText("");
                passwordField.setText("");
                userListModel.clear();
                java.util.List<User> users = UserManager.getAllUsers();
                for (User u : users) {
                    userListModel.addElement(u.getId() + ": " + u.getUsername() + " (" + u.getRole() + ")");
                }
            } catch (java.sql.SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error adding user: " + ex.getMessage());
            }
        });

        // Delete user action
        deleteUserButton.addActionListener(e -> {
            String selected = userList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select a user to delete.");
                return;
            }
            int userId = Integer.parseInt(selected.split(":")[0]);
            try {
                UserManager.deleteUser(userId);
                JOptionPane.showMessageDialog(this, "User deleted successfully.");
                userListModel.clear();
                java.util.List<User> users = UserManager.getAllUsers();
                for (User u : users) {
                    userListModel.addElement(u.getId() + ": " + u.getUsername() + " (" + u.getRole() + ")");
                }
            } catch (java.sql.SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting user: " + ex.getMessage());
            }
        });

        // Update role action
        updateRoleButton.addActionListener(e -> {
            String selected = userList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select a user to update role.");
                return;
            }
            int userId = Integer.parseInt(selected.split(":")[0]);
            String newRole = (String) roleComboBox.getSelectedItem();
            try {
                UserManager.updateUserRole(userId, newRole);
                JOptionPane.showMessageDialog(this, "User role updated successfully.");
                userListModel.clear();
                java.util.List<User> users = UserManager.getAllUsers();
                for (User u : users) {
                    userListModel.addElement(u.getId() + ": " + u.getUsername() + " (" + u.getRole() + ")");
                }
            } catch (java.sql.SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error updating user role: " + ex.getMessage());
            }
        });

        // Complaints panel for system admin
        JPanel complaintsPanel = new JPanel(new BorderLayout());
        complaintsPanel.setBorder(BorderFactory.createTitledBorder("All Complaints"));

        DefaultListModel<Complaint> complaintListModel = new DefaultListModel<>();
        JList<Complaint> complaintList = new JList<>(complaintListModel);
        complaintList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane complaintListScrollPane = new JScrollPane(complaintList);

        JTextArea complaintDescriptionArea = new JTextArea();
        complaintDescriptionArea.setEditable(false);
        complaintDescriptionArea.setLineWrap(true);
        complaintDescriptionArea.setWrapStyleWord(true);
        complaintDescriptionArea.setBorder(BorderFactory.createTitledBorder("Description"));

        complaintsPanel.add(complaintListScrollPane, BorderLayout.CENTER);
        complaintsPanel.add(new JScrollPane(complaintDescriptionArea), BorderLayout.SOUTH);

        panel.add(userManagementPanel, BorderLayout.NORTH);
        panel.add(complaintsPanel, BorderLayout.CENTER);

        // Load all complaints
        try {
            java.util.List<Complaint> complaints = ComplaintManager.getAllComplaints();
            for (Complaint c : complaints) {
                complaintListModel.addElement(c);
            }
        } catch (java.sql.SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading complaints: " + e.getMessage());
        }

        // Display complaint title, username and status in list
        complaintList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Complaint) {
                    Complaint complaint = (Complaint) value;
                    setText(complaint.getId() + ": " + complaint.getTitle() + " (User: " + complaint.getUsername() + ") - " + complaint.getStatus());
                }
                return this;
            }
        });

        // Update description area on selection
        complaintList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Complaint selected = complaintList.getSelectedValue();
                if (selected != null) {
                    complaintDescriptionArea.setText(selected.getDescription());
                } else {
                    complaintDescriptionArea.setText("");
                }
            }
        });

        contentPanel.add(panel);
    }

    private void showDepartmentOfficerDashboard() {
        JPanel panel = new JPanel(new BorderLayout());

        // Complaints list for department
        JPanel complaintsPanel = new JPanel(new BorderLayout());
        complaintsPanel.setBorder(BorderFactory.createTitledBorder("Department Complaints"));

        DefaultListModel<Complaint> complaintListModel = new DefaultListModel<>();
        JList<Complaint> complaintList = new JList<>(complaintListModel);
        complaintList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane complaintListScrollPane = new JScrollPane(complaintList);

        JTextArea complaintDescriptionArea = new JTextArea();
        complaintDescriptionArea.setEditable(false);
        complaintDescriptionArea.setLineWrap(true);
        complaintDescriptionArea.setWrapStyleWord(true);
        complaintDescriptionArea.setBorder(BorderFactory.createTitledBorder("Description"));

        complaintsPanel.add(complaintListScrollPane, BorderLayout.CENTER);
        complaintsPanel.add(new JScrollPane(complaintDescriptionArea), BorderLayout.SOUTH);

        panel.add(complaintsPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout());

        JButton resolveButton = new JButton("Mark as Resolved");
        JButton escalateButton = new JButton("Escalate Complaint");
        JButton communicateButton = new JButton("Send Message");

        buttonsPanel.add(resolveButton);
        buttonsPanel.add(escalateButton);
        buttonsPanel.add(communicateButton);

        panel.add(buttonsPanel, BorderLayout.SOUTH);

        // Load complaints for user's department
        try {
            int departmentId = user.getDepartmentId() > 0 ? user.getDepartmentId() : 1; // Default to 1 if not set
            java.util.List<Complaint> complaints = ComplaintManager.getComplaintsByDepartment(departmentId);
            for (Complaint c : complaints) {
                complaintListModel.addElement(c);
            }
        } catch (java.sql.SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading complaints: " + e.getMessage());
        }

        // Display complaint title, username and status in list
        complaintList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Complaint) {
                    Complaint complaint = (Complaint) value;
                    setText(complaint.getId() + ": " + complaint.getTitle() + " (User: " + complaint.getUsername() + ") - " + complaint.getStatus());
                }
                return this;
            }
        });

        // Update description area on selection
        complaintList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Complaint selected = complaintList.getSelectedValue();
                if (selected != null) {
                    complaintDescriptionArea.setText(selected.getDescription());
                } else {
                    complaintDescriptionArea.setText("");
                }
            }
        });

        // Resolve complaint action
        resolveButton.addActionListener(e -> {
            Complaint selected = complaintList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select a complaint to resolve.");
                return;
            }
            int complaintId = selected.getId();
            try {
                ComplaintManager.updateComplaintStatus(complaintId, "Resolved");
                JOptionPane.showMessageDialog(this, "Complaint marked as resolved.");
                complaintListModel.clear();
                int departmentId = user.getDepartmentId() > 0 ? user.getDepartmentId() : 1;
                java.util.List<Complaint> complaints = ComplaintManager.getComplaintsByDepartment(departmentId);
                for (Complaint c : complaints) {
                    complaintListModel.addElement(c);
                }
            } catch (java.sql.SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error updating complaint status: " + ex.getMessage());
            }
        });

        // Escalate complaint action
        escalateButton.addActionListener(e -> {
            Complaint selected = complaintList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select a complaint to escalate.");
                return;
            }
            int complaintId = selected.getId();
            try {
                ComplaintManager.updateComplaintStatus(complaintId, "Escalated");
                JOptionPane.showMessageDialog(this, "Complaint escalated.");
                complaintListModel.clear();
                int departmentId = user.getDepartmentId() > 0 ? user.getDepartmentId() : 1;
                java.util.List<Complaint> complaints = ComplaintManager.getComplaintsByDepartment(departmentId);
                for (Complaint c : complaints) {
                    complaintListModel.addElement(c);
                }
            } catch (java.sql.SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error updating complaint status: " + ex.getMessage());
            }
        });

        // Communicate action (simple input dialog)
        communicateButton.addActionListener(e -> {
            Complaint selected = complaintList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select a complaint to send message.");
                return;
            }
            String message = JOptionPane.showInputDialog(this, "Enter message to student:");
            if (message != null && !message.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Message sent: " + message);
                // TODO: Implement actual messaging system
            }
        });

        contentPanel.add(panel);
    }
}
