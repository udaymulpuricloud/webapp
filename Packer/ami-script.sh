#!/bin/bash


# Install Java (OpenJDK 17 in this example)
sudo apt-get install openjdk-17-jdk -y

# Install Maven
sudo apt-get install maven -y

# Set up PostgreSQL
sudo apt-get install postgresql -y

# Change password for the postgres user
sudo -u postgres psql -c "ALTER USER postgres PASSWORD 'Ud@R9603';"
