package com.csye6225.CloudAssignment2.Service;

import com.csye6225.CloudAssignment2.Model.Account;
import com.csye6225.CloudAssignment2.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

//    @Autowired
//    private PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account=accountRepository.findByEmail(email);

        if(account==null){
            throw new UsernameNotFoundException("User not found with email: " + email);


        }
        return new org.springframework.security.core.userdetails.User(
                account.getEmail(),
              account.getPassword(),
                new ArrayList<>());
    }
}
