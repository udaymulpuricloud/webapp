package com.csye6225.CloudAssignment2.Service;

import com.csye6225.CloudAssignment2.Model.Account;
import com.csye6225.CloudAssignment2.Repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AccountService {

    @Autowired
    private AccountRepository userRepository;

    public void LoadUsersFromCSV(String filepath){
        List<String[]> csvData= parseCSV(filepath);

        for(String[] userData:csvData){
            String firstname=userData[0];
            String lastname=userData[1];
            String email=userData[2];
            String password=userData[3];

            if ((firstname.length()<4 && firstname.length() > 20) ||(lastname.length()<4&& lastname.length() > 10)) {
                log.error("First name and last name must be betweenm 4 and 20 characters  for user with email: {}", email);
                continue;
            }
            if (!isValidEmail(email)) {
                log.error("Invalid email format for user with email: {}", email);
                continue;
            }


            if (password.length() < 8) {
                log.error("Password must be at least 8 characters long for user with email: {}", email);
                continue;
            }

            Account existing=userRepository.findByEmail(email);
            if(existing==null){
                Account user=new Account();
                user.setFirstname(firstname);
                user.setLastname(lastname);
                user.setEmail(email);
                user.setAccountCreated(Date.valueOf(LocalDate.now()));
                String hashpassword = BCrypt.hashpw(password,BCrypt.gensalt());
                user.setPassword(hashpassword);
                userRepository.save(user);

            }
            else
                log.error("User already present in the database with mail id :{}",email);
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

           if(val.length!=4){
               log.error("Invalid data in CSV,Count is not equal to 4,{}",val.length);
           }

            values.add(val);

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return values;
    }
    private static boolean isValidEmail(String email) {
        String regex ="^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(regex);
     }
}
