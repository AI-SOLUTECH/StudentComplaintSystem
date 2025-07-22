import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DetailedDBTest {
    public static void main(String[] args) {
        try {
            System.out.println("Testing database connection and setup...");
            
            Connection conn = DatabaseUtil.getConnection();
            System.out.println("✅ Database connection successful!");
            
            Statement stmt = conn.createStatement();
            
            // Test if we can create tables
            try {
                DatabaseUtil.initializeDatabase();
                System.out.println("✅ Database initialization successful!");
            } catch (Exception e) {
                System.err.println("❌ Database initialization failed:");
                e.printStackTrace();
            }
            
            // Check if tables exist
            try {
                ResultSet rs = stmt.executeQuery("SHOW TABLES");
                System.out.println("📋 Tables in database:");
                while (rs.next()) {
                    System.out.println("  - " + rs.getString(1));
                }
            } catch (SQLException e) {
                System.err.println("❌ Error checking tables:");
                e.printStackTrace();
            }
            
            conn.close();
            
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed:");
            e.printStackTrace();
            
            // Check if it's a database not found error
            if (e.getMessage().contains("Unknown database")) {
                System.out.println("\n💡 Solution: The database 'STU' doesn't exist.");
                System.out.println("Please create it first by running:");
                System.out.println("mysql -u root -p");
                System.out.println("CREATE DATABASE STU;");
            }
        }
    }
}