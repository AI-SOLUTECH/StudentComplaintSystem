-- Student Complaint System Database Setup
-- Run this script to set up the database manually if needed

-- Create database
CREATE DATABASE IF NOT EXISTS STU;
USE STU;

-- Create departments table
CREATE TABLE IF NOT EXISTS departments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    password VARCHAR(100) NOT NULL,
    role VARCHAR(50) DEFAULT 'User',
    department_id INT DEFAULT NULL,
    FOREIGN KEY (department_id) REFERENCES departments(id)
);

-- Create complaints table
CREATE TABLE IF NOT EXISTS complaints (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    department_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) DEFAULT 'Open',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    username VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (department_id) REFERENCES departments(id)
);

-- Insert default departments
INSERT IGNORE INTO departments (name, description) VALUES
('IT', 'Information Technology Department'),
('Academic', 'Academic Affairs Department'),
('Finance', 'Finance Department'),
('Administration', 'Administration Department');

-- Create a default system admin user (password: admin123)
-- Password hash for 'admin123' using SHA-256
INSERT IGNORE INTO users (username, full_name, email, password, role, department_id) VALUES
('admin', 'System Administrator', 'admin@school.edu', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'System Admin', 1);

-- Create sample users for testing (all passwords are 'password123')
-- Password hash for 'password123' using SHA-256
INSERT IGNORE INTO users (username, full_name, email, password, role, department_id) VALUES
('student1', 'John Doe', 'john.doe@student.edu', '8bb0cf6eb9b17d0f7d22b456f121257dc1254e1f01665370476383ea776df414', 'Student', NULL),
('officer1', 'Jane Smith', 'jane.smith@school.edu', '8bb0cf6eb9b17d0f7d22b456f121257dc1254e1f01665370476383ea776df414', 'Admin Officer', NULL),
('dept1', 'Mike Johnson', 'mike.johnson@school.edu', '8bb0cf6eb9b17d0f7d22b456f121257dc1254e1f01665370476383ea776df414', 'Department Officer', 1);

-- Create sample complaints for testing
INSERT IGNORE INTO complaints (user_id, department_id, title, description, status, username) VALUES
(2, 1, 'Computer Lab Issue', 'The computers in lab 101 are not working properly', 'Open', 'student1'),
(2, 2, 'Course Registration Problem', 'Unable to register for required courses', 'Pending', 'student1'),
(2, 3, 'Fee Payment Issue', 'Payment portal is not accepting my card', 'Resolved', 'student1');

-- Display setup completion message
SELECT 'Database setup completed successfully!' as Status;
SELECT 'Default users created:' as Info;
SELECT username, role, 'password123' as default_password FROM users WHERE username IN ('admin', 'student1', 'officer1', 'dept1');