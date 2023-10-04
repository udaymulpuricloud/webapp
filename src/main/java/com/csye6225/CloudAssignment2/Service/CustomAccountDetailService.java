package com.csye6225.CloudAssignment2.Service;

import com.csye6225.CloudAssignment2.Model.Account;
import com.csye6225.CloudAssignment2.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.security.core.userdetails.UserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Collections;

@Service
public class CustomAccountDetailService implements UserDetailsService{

    @Autowired
    HttpServletRequest request;

    @Autowired
    private AccountRepository accountRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account= accountRepository.findByEmail(email);
        if(account==null)

            throw new UsernameNotFoundException("Invalid username or password.");
        HttpSession session = request.getSession();
        session.setAttribute("accountId", account.getId());

        return new org.springframework.security.core.userdetails.User(
                account.getEmail(), account.getPassword(),Collections.emptyList());
    }
    }

