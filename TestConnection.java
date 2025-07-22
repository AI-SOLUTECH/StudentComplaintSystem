import java.sql.Connection;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseUtil.getConnection();
            System.out.println("Database connection successful!");
            conn.close();
        } catch (SQLException e) {
            System.err.println("Database connection failed:");
            e.printStackTrace();
        }
    }
}