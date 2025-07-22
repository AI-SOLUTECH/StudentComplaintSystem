#!/bin/bash

# Student Complaint System - Backup Script
# Creates a complete backup of the working application

BACKUP_DIR="StudentComplaintSystem_Backup_$(date +%Y%m%d_%H%M%S)"
SOURCE_DIR="/Users/nanakwame/CODE/StudentComplaintSystem"

echo "🔄 Creating backup of Student Complaint System..."
echo "📁 Backup directory: $BACKUP_DIR"

# Create backup directory
mkdir -p "$BACKUP_DIR"

# Copy all source files
echo "📋 Copying source files..."
cp -r "$SOURCE_DIR/src" "$BACKUP_DIR/"

# Copy MySQL connector
echo "🔗 Copying MySQL connector..."
cp -r "$SOURCE_DIR/mysql-connector-j-8.0.31" "$BACKUP_DIR/"

# Copy scripts and documentation
echo "📄 Copying scripts and documentation..."
cp "$SOURCE_DIR/run.sh" "$BACKUP_DIR/"
cp "$SOURCE_DIR/README.md" "$BACKUP_DIR/"
cp "$SOURCE_DIR/CHANGES_LOG.md" "$BACKUP_DIR/"
cp "$SOURCE_DIR/TestConnection.java" "$BACKUP_DIR/"
cp "$SOURCE_DIR/DetailedDBTest.java" "$BACKUP_DIR/"

# Copy this backup script
cp "$SOURCE_DIR/backup_script.sh" "$BACKUP_DIR/"

# Create a manifest file
echo "📝 Creating manifest..."
cat > "$BACKUP_DIR/MANIFEST.txt" << EOF
Student Complaint System - Complete Backup
==========================================

Backup Date: $(date)
Source Directory: $SOURCE_DIR
Backup Directory: $BACKUP_DIR

Files Included:
- All Java source files (src/)
- MySQL JDBC connector (mysql-connector-j-8.0.31/)
- Application launcher (run.sh)
- Documentation (README.md, CHANGES_LOG.md)
- Testing utilities (TestConnection.java, DetailedDBTest.java)
- This backup script (backup_script.sh)

Status: ✅ FULLY FUNCTIONAL APPLICATION
All features tested and working correctly.

To restore:
1. Copy all files to desired location
2. Ensure MySQL server is running with database 'STU'
3. Run: ./run.sh

Database Configuration:
- Host: localhost:3306
- Database: STU
- Username: root
- Password: VerNom@12

Features:
- Multi-role authentication system
- Complete complaint management
- Department-based access control
- User management system
- Automatic database initialization
EOF

# Create checksums for integrity verification
echo "🔐 Creating checksums..."
find "$BACKUP_DIR" -type f -exec md5 {} \; > "$BACKUP_DIR/checksums.md5"

# Make scripts executable
chmod +x "$BACKUP_DIR/run.sh"
chmod +x "$BACKUP_DIR/backup_script.sh"

echo "✅ Backup completed successfully!"
echo "📁 Backup location: $(pwd)/$BACKUP_DIR"
echo ""
echo "📋 Backup Contents:"
ls -la "$BACKUP_DIR"
echo ""
echo "🔍 To verify backup integrity:"
echo "   cd $BACKUP_DIR && md5 -c checksums.md5"
echo ""
echo "🚀 To run from backup:"
echo "   cd $BACKUP_DIR && ./run.sh"