package com.csye6225.CloudAssignment2.Config;

import com.csye6225.CloudAssignment2.Service.CustomAccountDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    CustomAccountDetailService customAccountDetailService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests((authz) -> authz.requestMatchers(new AntPathRequestMatcher("/healthz", "GET")).permitAll()
                .anyRequest()
                .authenticated());
        http.httpBasic(Customizer.withDefaults());
        http.csrf((csrf) -> csrf.disable());
        return http.build();
    }


}
