package com.csye6225.CloudAssignment2.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.jdbc.core.JdbcTemplate;

@RestController
@RequestMapping("/healthz")
public class HealthzController {

    @Autowired
    private JdbcTemplate jdbctemplate;

    @Autowired
    private HttpServletRequest request;

    @GetMapping()
    public ResponseEntity<Void> hello(@RequestBody(required=false)Object body) {
        HttpHeaders headers;
        headers=new HttpHeaders();
        headers.set("Pragma", "no-cache");
        headers.set("X-Content-Type-Options","nosniff");
        if(body!=null || request.getQueryString()!= null){
            return ResponseEntity.status(400).cacheControl(CacheControl.noCache().mustRevalidate()).headers(headers).build();
        }
        try {
            jdbctemplate.queryForObject("Select 1", Integer.class);
            return ResponseEntity.status(200).cacheControl(CacheControl.noCache().mustRevalidate()).headers(headers).build();


        } catch (DataAccessException e) {
            return ResponseEntity.status(503).cacheControl(CacheControl.noCache().mustRevalidate()).headers(headers).build();
        }
    }




}

