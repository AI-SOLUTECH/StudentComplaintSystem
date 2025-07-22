#!/bin/bash

# Student Complaint System - GitHub Setup Script
# This script automates the process of pushing your project to GitHub

echo "🚀 Student Complaint System - GitHub Setup"
echo "=========================================="
echo ""

# Check if git is installed
if ! command -v git &> /dev/null; then
    echo "❌ Git is not installed. Please install Git first."
    echo "   Visit: https://git-scm.com/downloads"
    exit 1
fi

echo "✅ Git is installed"

# Check Git configuration
echo ""
echo "🔧 Checking Git configuration..."
CURRENT_NAME=$(git config --global user.name 2>/dev/null || echo "")
CURRENT_EMAIL=$(git config --global user.email 2>/dev/null || echo "")

if [[ -z "$CURRENT_NAME" || -z "$CURRENT_EMAIL" ]]; then
    echo "⚠️  Git user information is not configured!"
    echo "   Current name:  ${CURRENT_NAME:-'Not set'}"
    echo "   Current email: ${CURRENT_EMAIL:-'Not set'}"
    echo ""
    echo "🔧 You need to configure Git before pushing to GitHub."
    echo ""
    read -p "🤔 Do you want to configure Git now? (Y/n): " CONFIGURE_GIT
    
    if [[ ! $CONFIGURE_GIT =~ ^[Nn]$ ]]; then
        echo ""
        echo "📝 Let's configure your Git user information:"
        
        # Get user's name
        while true; do
            read -p "👤 Enter your full name (e.g., John Doe): " USER_NAME
            if [[ -n "$USER_NAME" ]]; then
                break
            else
                echo "❌ Name cannot be empty. Please try again."
            fi
        done
        
        # Get user's email
        while true; do
            read -p "📧 Enter your email address: " USER_EMAIL
            if [[ "$USER_EMAIL" =~ ^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$ ]]; then
                break
            else
                echo "❌ Please enter a valid email address."
            fi
        done
        
        # Configure Git
        git config --global user.name "$USER_NAME"
        git config --global user.email "$USER_EMAIL"
        
        echo "✅ Git configured successfully!"
        echo "   Name:  $(git config --global user.name)"
        echo "   Email: $(git config --global user.email)"
    else
        echo ""
        echo "❌ Git configuration is required. Please run:"
        echo "   ./configure_git.sh"
        echo "   or manually set:"
        echo "   git config --global user.name \"Your Name\""
        echo "   git config --global user.email \"your.email@example.com\""
        exit 1
    fi
else
    echo "✅ Git is configured:"
    echo "   Name:  $CURRENT_NAME"
    echo "   Email: $CURRENT_EMAIL"
fi

# Get GitHub username
echo ""
read -p "📝 Enter your GitHub username: " GITHUB_USERNAME

if [ -z "$GITHUB_USERNAME" ]; then
    echo "❌ GitHub username is required"
    exit 1
fi

# Get repository name (default: StudentComplaintSystem)
echo ""
read -p "📝 Enter repository name (default: StudentComplaintSystem): " REPO_NAME
REPO_NAME=${REPO_NAME:-StudentComplaintSystem}

echo ""
echo "📋 Setup Summary:"
echo "   GitHub Username: $GITHUB_USERNAME"
echo "   Repository Name: $REPO_NAME"
echo "   Repository URL: https://github.com/$GITHUB_USERNAME/$REPO_NAME"
echo ""

read -p "🤔 Continue with setup? (y/N): " CONFIRM
if [[ ! $CONFIRM =~ ^[Yy]$ ]]; then
    echo "❌ Setup cancelled"
    exit 1
fi

echo ""
echo "🔄 Setting up Git repository..."

# Initialize git repository if not already initialized
if [ ! -d ".git" ]; then
    git init
    echo "✅ Git repository initialized"
else
    echo "✅ Git repository already exists"
fi

# Check if there are any changes to commit
if [ -n "$(git status --porcelain)" ]; then
    echo "📦 Adding files to Git..."
    git add .
    
    echo "💾 Creating initial commit..."
    git commit -m "Initial commit: Complete Student Complaint System

Features:
- Multi-role authentication (Student, Admin Officer, Department Officer, System Admin)
- Complete complaint management system
- Department-based access control
- Secure password hashing (SHA-256)
- Automatic database initialization
- Comprehensive user management
- Role-based dashboards
- MySQL database integration

Tech Stack: Java Swing, MySQL, JDBC

🎯 Ready for production use with comprehensive documentation and testing utilities."
    
    echo "✅ Initial commit created"
else
    echo "✅ No changes to commit"
fi

# Set main branch
git branch -M main

# Add remote origin
REPO_URL="https://github.com/$GITHUB_USERNAME/$REPO_NAME.git"

# Remove existing origin if it exists
git remote remove origin 2>/dev/null

git remote add origin $REPO_URL
echo "✅ Remote origin added: $REPO_URL"

echo ""
echo "🌐 Next steps:"
echo "1. Go to GitHub.com and create a new repository named '$REPO_NAME'"
echo "2. Make sure the repository is empty (don't initialize with README)"
echo "3. Come back here and press Enter to continue..."
echo ""
read -p "⏳ Press Enter after creating the GitHub repository..."

echo ""
echo "🚀 Pushing to GitHub..."

# Push to GitHub
if git push -u origin main; then
    echo ""
    echo "🎉 SUCCESS! Your Student Complaint System has been uploaded to GitHub!"
    echo ""
    echo "📍 Repository URL: https://github.com/$GITHUB_USERNAME/$REPO_NAME"
    echo ""
    echo "🎯 What's included:"
    echo "   ✅ Complete Java source code"
    echo "   ✅ MySQL JDBC driver"
    echo "   ✅ Database setup scripts"
    echo "   ✅ Comprehensive documentation"
    echo "   ✅ Installation guides"
    echo "   ✅ Testing utilities"
    echo ""
    echo "🔗 Share your repository:"
    echo "   git clone https://github.com/$GITHUB_USERNAME/$REPO_NAME.git"
    echo ""
    echo "📚 Next steps:"
    echo "   1. Add repository description on GitHub"
    echo "   2. Add topics: java, swing, mysql, complaint-management"
    echo "   3. Create a release (v1.0.0)"
    echo "   4. Share with others!"
    echo ""
    echo "🌟 Your project is now live on GitHub!"
else
    echo ""
    echo "❌ Failed to push to GitHub"
    echo ""
    echo "🔧 Troubleshooting:"
    echo "   1. Make sure you created the repository on GitHub"
    echo "   2. Check your internet connection"
    echo "   3. Verify your GitHub credentials"
    echo "   4. Try running: git push -u origin main"
    echo ""
    echo "💡 Manual push command:"
    echo "   git remote add origin $REPO_URL"
    echo "   git push -u origin main"
fi