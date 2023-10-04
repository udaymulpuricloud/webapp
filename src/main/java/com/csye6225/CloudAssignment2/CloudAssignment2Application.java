package com.csye6225.CloudAssignment2;

import com.csye6225.CloudAssignment2.Service.AccountService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CloudAssignment2Application {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(CloudAssignment2Application.class, args);

        AccountService userService = context.getBean(AccountService.class);

        String filepath="opt/users.csv";
        userService.LoadUsersFromCSV(filepath);
    }


}
