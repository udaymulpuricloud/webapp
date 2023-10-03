package com.csye6225.CloudAssignment2;

import com.csye6225.CloudAssignment2.Service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CloudAssignment2Application {

    public static void main(String[] args) {

        SpringApplication.run(CloudAssignment2Application.class, args);
        String filepath="/opt/user.csv";
        UserService.LoadUsersFromCSV(filepath);
    }


}
