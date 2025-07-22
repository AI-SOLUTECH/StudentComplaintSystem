# Student Complaint System

A comprehensive Java Swing application for managing student complaints in educational institutions.

Features

### Multi-Role System
- **Students**: Submit and track complaints
- **Admin Officers**: Manage all complaints system-wide
- **Department Officers**: Handle department-specific complaints
- **System Admins**: Full user and system management

Core Functionality
- Secure user authentication with SHA-256 password hashing
- Role-based access control
- Department-based complaint routing
- Real-time complaint status tracking
- Complete user management system
- Automatic database initialization

Technical Stack

- **Language**: Java
- **GUI Framework**: Swing
- **Database**: MySQL
- **JDBC Driver**: MySQL Connector/J 8.0.31
- **Architecture**: MVC Pattern

Prerequisites

- Java JDK 8 or higher
- MySQL Server 5.7 or higher
- MySQL database named `STU` with credentials:
  - Host: localhost:3306
  - Username: root
  - Password: VerNom@12

 Quick Start

1. Clone the repository
```bash
git clone https://github.com/YOUR_USERNAME/StudentComplaintSystem.git
cd StudentComplaintSystem
```

Alternative: Download ZIP
- Download from GitHub and extract to your desired location

 2. Download dependencies
```bash
./download_dependencies.sh
```

3. Run the application
```bash
./run.sh
```

Or manually:
```bash
java -cp ".:mysql-connector-j-8.0.31/mysql-connector-j-8.0.31.jar:src" MainApp
```

3. First-time setup
1. Click "Register" to create your first user account
2. Choose your role (Student, Admin Officer, Department Officer, System Admin)
3. Complete registration and login
4. Access your role-specific dashboard

Database Schema

The application automatically creates the following tables:

departments
- Default departments: IT, Academic, Finance, Administration

users
- Stores user credentials and role information
- Links to departments for Department Officers

complaints
- Tracks all complaints with status and timestamps
- Links to users and departments

User Roles & Permissions

 Student
- Submit new complaints
- View personal complaint history
- Track complaint status

Admin Officer
- View all complaints system-wide
- Resolve, escalate, or delete complaints
- Manage complaint workflow

Department Officer
- View complaints for assigned department
- Resolve or escalate department complaints
- Communicate with students

System Admin
- Full user management (add, delete, update roles)
- System-wide complaint oversight
- Administrative controls

Configuration

Database connection settings are in `src/DatabaseUtil.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/STU?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
private static final String USER = "root";
private static final String PASSWORD = "VerNom@12";
```

Testing

Test database connection:
```bash
java -cp ".:mysql-connector-j-8.0.31/mysql-connector-j-8.0.31.jar:src" TestConnection
```

Comprehensive database test:
```bash
java -cp ".:mysql-connector-j-8.0.31/mysql-connector-j-8.0.31.jar:src" DetailedDBTest
```

Project Structure

```
StudentComplaintSystem/
├── src/
│   ├── MainApp.java              # Application entry point
│   ├── DatabaseUtil.java         # Database connection & initialization
│   ├── LoginPanel.java           # Authentication interface
│   ├── RegistrationPanel.java    # User registration interface
│   ├── DashboardPanel.java       # Role-based dashboards
│   ├── User.java                 # User data model
│   ├── Complaint.java            # Complaint data model
│   ├── UserManager.java          # User operations
│   ├── ComplaintManager.java     # Complaint operations
│   └── PasswordUtil.java         # Password hashing
├── mysql-connector-j-8.0.31/     # JDBC driver
├── run.sh                        # Application launcher
├── TestConnection.java           # Database connection test
├── DetailedDBTest.java          # Comprehensive database test
├── README.md                     # This file
└── CHANGES_LOG.md               # Detailed change history
```

Troubleshooting

Database Connection Issues
1. Ensure MySQL server is running
2. Verify database credentials in `DatabaseUtil.java`
3. Check if database `STU` exists (auto-created if not)

Application Won't Start
1. Verify Java version: `java -version`
2. Check classpath includes MySQL connector
3. Run database connection test

GUI Issues
1. Ensure display is available (not running headless)
2. Check Java Swing support
3. Verify window manager compatibility

License

This project is for Gimpa educational purposes.

 GitHub Repository

Uploading to GitHub
To push this project to your GitHub account:

1. Automated Setup** (Recommended):
   ```bash
   ./setup_github.sh
   ```

2. Manual Setup**:
   ```bash
   git init
   git add .
   git commit -m "Initial commit: Student Complaint System"
   git remote add origin https://github.com/YOUR_USERNAME/StudentComplaintSystem.git
   git push -u origin main
   ```

3. Detailed Instructions**: See [GITHUB_SETUP.md](GITHUB_SETUP.md)

Repository Features
- Complete source code with documentation
- Database setup scripts included
- Installation guides for all platforms
- Testing utilities and examples
- Comprehensive README and documentation

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📞 Support

For issues or questions, please check the `CHANGES_LOG.md` for detailed implementation notes.

---

**Status**: ✅ Production Ready - All features tested and working
**Last Updated**: $(date)
