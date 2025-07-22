import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class MainApp extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private User currentUser;

    public MainApp() {
        setTitle("Student Complaint System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Update complaint statuses to Pending if null or not Resolved
        try {
            ComplaintManager.updatePendingStatuses();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        LoginPanel loginPanel = new LoginPanel(this);
        loginPanel.setName("login");
        RegistrationPanel registrationPanel = new RegistrationPanel(this);
        registrationPanel.setName("register");
        DashboardPanel dashboardPanel = new DashboardPanel(this);
        dashboardPanel.setName("dashboard");

        mainPanel.add(loginPanel, "login");
        mainPanel.add(registrationPanel, "register");
        mainPanel.add(dashboardPanel, "dashboard");

        add(mainPanel);
        showLogin();
    }

    public void showLogin() {
        cardLayout.show(mainPanel, "login");
    }

    public void showRegistration() {
        cardLayout.show(mainPanel, "register");
    }

    public void showDashboard(User user) {
        this.currentUser = user;
        ((DashboardPanel) getPanel("dashboard")).setUser(user);
        cardLayout.show(mainPanel, "dashboard");
    }

    public JPanel getPanel(String name) {
        for (Component comp : mainPanel.getComponents()) {
            if (comp instanceof JPanel && name.equals(comp.getName())) {
                return (JPanel) comp;
            }
        }
        return null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public static void main(String[] args) {
        // Initialize database
        DatabaseUtil.initializeDatabase();

        // Set Nimbus Look and Feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, fall back to default
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainApp app = new MainApp();
            app.setVisible(true);
        });
    }
}
