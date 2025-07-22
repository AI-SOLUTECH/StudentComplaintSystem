import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserManager {

    public static void addUser(String username, String fullName, String email, String password, String role) throws SQLException {
        String sql = "INSERT INTO users (username, full_name, email, password, role) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, fullName);
            stmt.setString(3, email);
            stmt.setString(4, PasswordUtil.hashPassword(password));
            stmt.setString(5, role);
            stmt.executeUpdate();
        }
    }

    public static List<User> getAllUsers() throws SQLException {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                users.add(mapRowToUser(rs));
            }
        }
        return users;
    }

    public static void updateUserRole(int userId, String role) throws SQLException {
        String sql = "UPDATE users SET role = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, role);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    public static void deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    private static User mapRowToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        user.setDepartmentId(rs.getInt("department_id"));
        return user;
    }
}
