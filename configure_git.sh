#!/bin/bash

# Git Configuration Script for Student Complaint System
# This script helps configure Git user information before pushing to GitHub

echo "🔧 Git Configuration Setup"
echo "=========================="
echo ""

# Check if git is installed
if ! command -v git &> /dev/null; then
    echo "❌ Git is not installed. Please install Git first."
    echo "   Visit: https://git-scm.com/downloads"
    exit 1
fi

echo "✅ Git is installed"
echo ""

# Check current git configuration
echo "📋 Current Git Configuration:"
CURRENT_NAME=$(git config --global user.name 2>/dev/null || echo "Not set")
CURRENT_EMAIL=$(git config --global user.email 2>/dev/null || echo "Not set")

echo "   Name:  $CURRENT_NAME"
echo "   Email: $CURRENT_EMAIL"
echo ""

# Ask if user wants to update configuration
if [[ "$CURRENT_NAME" != "Not set" && "$CURRENT_EMAIL" != "Not set" ]]; then
    read -p "🤔 Git is already configured. Do you want to update it? (y/N): " UPDATE_CONFIG
    if [[ ! $UPDATE_CONFIG =~ ^[Yy]$ ]]; then
        echo "✅ Using existing Git configuration"
        echo ""
        echo "🎯 You're ready to push to GitHub!"
        echo "   Run: ./setup_github.sh"
        exit 0
    fi
fi

echo "📝 Let's configure your Git user information:"
echo ""

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
    read -p "📧 Enter your email address (e.g., john.doe@email.com): " USER_EMAIL
    # Basic email validation
    if [[ "$USER_EMAIL" =~ ^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$ ]]; then
        break
    else
        echo "❌ Please enter a valid email address."
    fi
done

echo ""
echo "📋 Configuration Summary:"
echo "   Name:  $USER_NAME"
echo "   Email: $USER_EMAIL"
echo ""

read -p "✅ Is this information correct? (Y/n): " CONFIRM
if [[ $CONFIRM =~ ^[Nn]$ ]]; then
    echo "❌ Configuration cancelled. Please run the script again."
    exit 1
fi

echo ""
echo "🔧 Configuring Git..."

# Set git configuration globally
git config --global user.name "$USER_NAME"
git config --global user.email "$USER_EMAIL"

# Verify configuration was set
VERIFY_NAME=$(git config --global user.name)
VERIFY_EMAIL=$(git config --global user.email)

if [[ "$VERIFY_NAME" == "$USER_NAME" && "$VERIFY_EMAIL" == "$USER_EMAIL" ]]; then
    echo "✅ Git configuration successful!"
    echo ""
    echo "📋 Your Git is now configured with:"
    echo "   Name:  $VERIFY_NAME"
    echo "   Email: $VERIFY_EMAIL"
    echo ""
    echo "🎯 Next Steps:"
    echo "   1. Run: ./setup_github.sh"
    echo "   2. Or manually push to GitHub"
    echo ""
    echo "💡 This configuration will be used for all your Git commits."
else
    echo "❌ Configuration failed. Please try again or set manually:"
    echo "   git config --global user.name \"Your Name\""
    echo "   git config --global user.email \"your.email@example.com\""
fi