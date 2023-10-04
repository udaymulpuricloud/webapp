package com.csye6225.CloudAssignment2.Config;

import com.csye6225.CloudAssignment2.Service.CustomAccountDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ValidationConfig implements AuthenticationProvider {
    @Autowired
    private CustomAccountDetailService customAccountDetailsService;


    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username=authentication.getName();
        String password=authentication.getCredentials().toString();
        System.out.println("Received username: " + username);
        System.out.println("Loaded password: " + password);
        UserDetails user = customAccountDetailsService.loadUserByUsername(username);

        if(passwordEncoder.matches(password,user.getPassword())){
            return new UsernamePasswordAuthenticationToken(user, password,user.getAuthorities());
        }else {
            throw new BadCredentialsException("Invalid credentials");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
