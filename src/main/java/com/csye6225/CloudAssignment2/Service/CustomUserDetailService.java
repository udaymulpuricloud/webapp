package com.csye6225.CloudAssignment2.Service;

import com.csye6225.CloudAssignment2.Model.Account;
import com.csye6225.CloudAssignment2.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailService {

    @Autowired
    private AccountRepository accountRepository;

    public Account loadUserByUsername(String email)  {


        return accountRepository.findByEmail(email);
    }
}
