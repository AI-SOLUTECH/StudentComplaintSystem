import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/STU?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "VerNom@12";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Failed to create database connection.");
            throw e;
        }
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            // Create departments table first (referenced by users table)
            String sqlDepartments = "CREATE TABLE IF NOT EXISTS departments (" +
                         "id INT AUTO_INCREMENT PRIMARY KEY," +
                         "name VARCHAR(100) NOT NULL UNIQUE," +
                         "description TEXT" +
                         ")";
            stmt.executeUpdate(sqlDepartments);

            // Create users table if it does not exist
            String sqlUsers = "CREATE TABLE IF NOT EXISTS users (" +
                         "id INT AUTO_INCREMENT PRIMARY KEY," +
                         "username VARCHAR(50) NOT NULL UNIQUE," +
                         "full_name VARCHAR(100) NOT NULL," +
                         "email VARCHAR(100)," +
                         "password VARCHAR(100) NOT NULL," +
                         "role VARCHAR(50) DEFAULT 'User'," +
                         "department_id INT DEFAULT NULL" +
                         ")";
            stmt.executeUpdate(sqlUsers);
            
            // Check if department_id column exists, add it if not
            try {
                ResultSet rs = stmt.executeQuery("SHOW COLUMNS FROM users LIKE 'department_id'");
                if (!rs.next()) {
                    // Column doesn't exist, add it
                    stmt.executeUpdate("ALTER TABLE users ADD COLUMN department_id INT DEFAULT NULL");
                    System.out.println("Added department_id column to users table");
                }
            } catch (SQLException e) {
                System.err.println("Warning: Could not check/add department_id column: " + e.getMessage());
            }
            
            // Add foreign key constraint only if it doesn't exist
            try {
                // Check if the foreign key constraint already exists
                String checkConstraint = "SELECT COUNT(*) FROM information_schema.KEY_COLUMN_USAGE " +
                                        "WHERE CONSTRAINT_NAME = 'fk_user_department' " +
                                        "AND TABLE_SCHEMA = DATABASE() " +
                                        "AND TABLE_NAME = 'users'";
                ResultSet constraintRs = stmt.executeQuery(checkConstraint);
                
                boolean constraintExists = false;
                if (constraintRs.next() && constraintRs.getInt(1) > 0) {
                    constraintExists = true;
                }
                
                if (!constraintExists) {
                    String addForeignKey = "ALTER TABLE users ADD CONSTRAINT fk_user_department " +
                                         "FOREIGN KEY (department_id) REFERENCES departments(id)";
                    stmt.executeUpdate(addForeignKey);
                    System.out.println("Added foreign key constraint fk_user_department");
                }
            } catch (SQLException e) {
                System.err.println("Warning: Could not add foreign key constraint: " + e.getMessage());
            }

            // Create complaints table if it does not exist
            String sqlComplaints = "CREATE TABLE IF NOT EXISTS complaints (" +
                         "id INT AUTO_INCREMENT PRIMARY KEY," +
                         "user_id INT NOT NULL," +
                         "department_id INT NOT NULL," +
                         "title VARCHAR(255) NOT NULL," +
                         "description TEXT," +
                         "status VARCHAR(50) DEFAULT 'Open'," +
                         "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                         "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                         "FOREIGN KEY (user_id) REFERENCES users(id)," +
                         "FOREIGN KEY (department_id) REFERENCES departments(id)" +
                         ")";
            stmt.executeUpdate(sqlComplaints);

            // Insert default departments if they don't exist
            String checkDepartments = "SELECT COUNT(*) FROM departments";
            ResultSet rs = stmt.executeQuery(checkDepartments);
            if (rs.next() && rs.getInt(1) == 0) {
                String[] departments = {"IT", "Academic", "Finance", "Administration"};
                String[] descriptions = {
                    "Information Technology Department",
                    "Academic Affairs Department", 
                    "Finance Department",
                    "Administration Department"
                };
                
                String insertDept = "INSERT INTO departments (name, description) VALUES (?, ?)";
                try (PreparedStatement deptStmt = conn.prepareStatement(insertDept)) {
                    for (int i = 0; i < departments.length; i++) {
                        deptStmt.setString(1, departments[i]);
                        deptStmt.setString(2, descriptions[i]);
                        deptStmt.executeUpdate();
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Database initialization error:");
            e.printStackTrace();
        }
    }
}
