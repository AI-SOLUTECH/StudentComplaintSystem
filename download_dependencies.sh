#!/bin/bash

# Student Complaint System - Dependencies Download Script
# This script downloads the required MySQL JDBC connector

echo "📦 Downloading MySQL JDBC Connector..."
echo "======================================"

# Create directory if it doesn't exist
mkdir -p mysql-connector-j-8.0.31

# Download MySQL Connector/J if it doesn't exist
if [ ! -f "mysql-connector-j-8.0.31/mysql-connector-j-8.0.31.jar" ]; then
    echo "🔄 Downloading MySQL Connector/J 8.0.31..."
    
    # Try to download from Maven Central
    curl -L -o mysql-connector-j-8.0.31/mysql-connector-j-8.0.31.jar \
        "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.31/mysql-connector-j-8.0.31.jar"
    
    if [ $? -eq 0 ]; then
        echo "✅ MySQL Connector downloaded successfully!"
    else
        echo "❌ Failed to download MySQL Connector"
        echo "💡 Please download manually from:"
        echo "   https://dev.mysql.com/downloads/connector/j/"
        echo "   Extract mysql-connector-j-8.0.31.jar to mysql-connector-j-8.0.31/ folder"
        exit 1
    fi
else
    echo "✅ MySQL Connector already exists"
fi

echo ""
echo "🎯 Dependencies ready!"
echo "   You can now run: ./run.sh"