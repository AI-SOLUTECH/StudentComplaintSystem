import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComplaintManager {

    public static void addComplaint(int userId, int departmentId, String title, String description) throws SQLException {
        String sql = "INSERT INTO complaints (user_id, department_id, title, description) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, departmentId);
            stmt.setString(3, title);
            stmt.setString(4, description);
            stmt.executeUpdate();
        }
    }

    public static List<Complaint> getComplaintsByUser(int userId) throws SQLException {
        String sql = "SELECT c.*, u.username FROM complaints c JOIN users u ON c.user_id = u.id WHERE c.user_id = ?";
        List<Complaint> complaints = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                complaints.add(mapRowToComplaint(rs));
            }
        }
        return complaints;
    }

    public static List<Complaint> getComplaintsByDepartment(int departmentId) throws SQLException {
        String sql = "SELECT c.*, u.username FROM complaints c JOIN users u ON c.user_id = u.id WHERE c.department_id = ?";
        List<Complaint> complaints = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, departmentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                complaints.add(mapRowToComplaint(rs));
            }
        }
        return complaints;
    }

    public static List<Complaint> getAllComplaints() throws SQLException {
        String sql = "SELECT c.*, u.username FROM complaints c JOIN users u ON c.user_id = u.id";
        List<Complaint> complaints = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                complaints.add(mapRowToComplaint(rs));
            }
        }
        return complaints;
    }

    public static void updateComplaintStatus(int complaintId, String status) throws SQLException {
        String sql = "UPDATE complaints SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, complaintId);
            stmt.executeUpdate();
        }
    }

    public static void updateComplaintDescription(int complaintId, String description) throws SQLException {
        String sql = "UPDATE complaints SET description = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, description);
            stmt.setInt(2, complaintId);
            stmt.executeUpdate();
        }
    }

    private static Complaint mapRowToComplaint(ResultSet rs) throws SQLException {
        Complaint complaint = new Complaint();
        complaint.setId(rs.getInt("id"));
        complaint.setUserId(rs.getInt("user_id"));
        complaint.setDepartmentId(rs.getInt("department_id"));
        complaint.setTitle(rs.getString("title"));
        complaint.setDescription(rs.getString("description"));
        String status = rs.getString("status");
        if (status == null || status.trim().isEmpty()) {
            status = "Pending";
        }
        complaint.setStatus(status);
        complaint.setCreatedAt(rs.getTimestamp("created_at"));
        complaint.setUpdatedAt(rs.getTimestamp("updated_at"));
        complaint.setUsername(rs.getString("username"));
        return complaint;
    }

    public static void updatePendingStatuses() throws SQLException {
        String sql = "UPDATE complaints SET status = 'Pending' WHERE status IS NULL OR status != 'Resolved'";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
    }

    public static void deleteComplaint(int complaintId) throws SQLException {
        String sql = "DELETE FROM complaints WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, complaintId);
            stmt.executeUpdate();
        }
    }
}
