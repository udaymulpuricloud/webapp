#!/bin/bash

#
## Install Java (OpenJDK 17 in this example)
#sudo apt-get install openjdk-17-jdk -y
#
## Install Maven
#sudo apt-get install maven -y


#wget https://s3.amazonaws.com/amazoncloudwatch-agent/linux/amd64/latest/amazon-cloudwatch-agent.rpm
#sudo rpm -U ./amazon-cloudwatch-agent.rpm

wget https://s3.amazonaws.com/amazoncloudwatch-agent/debian/amd64/latest/amazon-cloudwatch-agent.deb
sudo dpkg -i amazon-cloudwatch-agent.deb

sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config\
-m ec2\
-c file:/opt/cloudwatch-config.json \
-s
# Set up PostgreSQL
#sudo apt-get install postgresql -y
#
#
#sudo systemctl start postgresql    # Start the PostgreSQL service
#sudo systemctl enable postgresql   # Enable PostgreSQL to start on boot
#sudo -u postgres psql -c "ALTER USER postgres WITH PASSWORD 'UdaR9603';"
#sudo -u postgres psql -c "CREATE DATABASE csye6225;"
