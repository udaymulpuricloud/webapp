package com.csye6225.CloudAssignment2;

import com.csye6225.CloudAssignment2.Service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CloudAssignment2Application {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(CloudAssignment2Application.class, args);

        UserService userService = context.getBean(UserService.class);

        String filepath="opt/users.csv";
        userService.LoadUsersFromCSV(filepath);
    }


}
