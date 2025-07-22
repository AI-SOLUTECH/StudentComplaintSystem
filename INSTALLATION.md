# 🚀 Student Complaint System - Installation Guide

## Quick Start (5 minutes setup)

### 1. **Prerequisites**
- Java JDK 8 or higher
- MySQL Server 5.7 or higher
- Git (for cloning)

### 2. **Clone the Repository**
```bash
git clone https://github.com/YOUR_USERNAME/StudentComplaintSystem.git
cd StudentComplaintSystem
```

### 3. **Database Setup**

#### Option A: Automatic Setup (Recommended)
The application will automatically create the database and tables on first run.

#### Option B: Manual Setup
```bash
# Connect to MySQL
mysql -u root -p

# Run the setup script
source DATABASE_SETUP.sql

# Or copy-paste the contents of DATABASE_SETUP.sql
```

### 4. **Configure Database Connection**
Edit `src/DatabaseUtil.java` if needed:
```java
private static final String URL = "jdbc:mysql://localhost:3306/STU?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
private static final String USER = "root";  // Your MySQL username
private static final String PASSWORD = "VerNom@12";  // Your MySQL password
```

### 5. **Run the Application**
```bash
# Make script executable (Linux/macOS)
chmod +x run.sh

# Run the application
./run.sh

# Or manually:
# Windows: java -cp ".;mysql-connector-j-8.0.31\mysql-connector-j-8.0.31.jar;src" MainApp
# Linux/macOS: java -cp ".:mysql-connector-j-8.0.31/mysql-connector-j-8.0.31.jar:src" MainApp
```

## 🎯 Default Login Credentials

After running `DATABASE_SETUP.sql`, you can use these test accounts:

| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | System Admin |
| student1 | password123 | Student |
| officer1 | password123 | Admin Officer |
| dept1 | password123 | Department Officer |

## 🔧 Troubleshooting

### Database Connection Issues
```bash
# Test database connection
java -cp ".:mysql-connector-j-8.0.31/mysql-connector-j-8.0.31.jar:src" TestConnection
```

### Compilation Issues
```bash
# Compile manually
javac -cp ".:mysql-connector-j-8.0.31/mysql-connector-j-8.0.31.jar" src/*.java
```

## 📁 Project Structure
```
StudentComplaintSystem/
├── src/                    # Java source files
├── mysql-connector-j-8.0.31/  # JDBC driver
├── DATABASE_SETUP.sql      # Database initialization script
├── run.sh                  # Application launcher
├── README.md              # Main documentation
└── INSTALLATION.md        # This file
```

## 🎊 Success Indicators
- ✅ Application window appears
- ✅ Can login with default credentials
- ✅ Database tables are created
- ✅ Can register new users and submit complaints

For detailed features and usage, see [README.md](README.md).