#!/bin/bash


# Install Java (OpenJDK 17 in this example)
sudo apt-get install openjdk-17-jdk -y

# Install Maven
sudo apt-get install maven -y

# Set up PostgreSQL
sudo apt-get install postgresql -y


sudo systemctl start postgresql    # Start the PostgreSQL service
sudo systemctl enable postgresql   # Enable PostgreSQL to start on boot
sudo -u postgres psql -c "ALTER USER postgres WITH PASSWORD 'Ud@R9603';"
sudo -u postgres psql -c "CREATE DATABASE cloudmydb;"
