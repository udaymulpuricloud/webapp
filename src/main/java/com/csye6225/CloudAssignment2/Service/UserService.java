package com.csye6225.CloudAssignment2.Service;

import com.csye6225.CloudAssignment2.Model.User;
import com.csye6225.CloudAssignment2.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void LoadUsersFromCSV(String filepath){
        List<String[]> csvData= parseCSV(filepath);

        for(String[] userData:csvData){
            String firstname=userData[0];
            String lastname=userData[1];
            String email=userData[2];
            String password=userData[3];

            User existing=userRepository.findByEmail(email);
            if(existing==null){
                User user=new User();
                user.setFirstname(firstname);
                user.setLastname(lastname);
                user.setEmail(email);
                String hashpassword = new BCryptPasswordEncoder().encode(password);
                user.setPassword(hashpassword);
                userRepository.save(user);

            }
        }
    }

    private static List<String[]> parseCSV(String filepath) {
        List<String[]> values= new ArrayList<>();

        try{
            BufferedReader br= new BufferedReader(new FileReader(filepath));
            String line;
            br.readLine();
            while((line= br.readLine())!=null){
           String[] val=line.split(",");
            values.add(val);

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return values;
    }
}
