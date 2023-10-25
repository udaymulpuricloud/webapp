First we need to hit mvn clean install 
need to hit mvn spring-boot:run
postgressql is running on local 5432 port
Created the webapp with one basic health check end point and several other endpoints
In this endpoints user can create the assignment
user can view the assignment
user can delete the assignment
user data and mail address will be taken from /opt with users.csv file and then application will store the data into database 
when user is trying to create the assignment , the application will check with the user data in db then only it will allow the person to edit

3 github actions will the running for the application when the code is pushed
2 of them will start running on pull request and other on successfully pushing the code into org branch

created a packer folder where it is used to create the AWS AMI with the webapp jar file 
created a service file which will be used as systemd file , and it will copied to ami 
once the instance is up , the application will run automatically



