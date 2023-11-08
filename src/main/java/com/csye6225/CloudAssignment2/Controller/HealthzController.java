package com.csye6225.CloudAssignment2.Controller;

import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
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
import org.slf4j.Logger;

@RestController
@RequestMapping("/healthz")
public class HealthzController {

    @Autowired
    private JdbcTemplate jdbctemplate;
Logger logger= LoggerFactory.getLogger(HealthzController.class);
    @Autowired
    private HttpServletRequest request;
private final StatsDClient statsDClient = new NonBlockingStatsDClient("metricn","localhost",8125);
    @GetMapping()
    public ResponseEntity<Void> hello(@RequestBody(required=false)Object body) {
        HttpHeaders headers;
        headers=new HttpHeaders();
        headers.set("Pragma", "no-cache");
        headers.set("X-Content-Type-Options","nosniff");
        statsDClient.incrementCounter("healthz");
        if(body!=null || request.getQueryString()!= null){
            logger.error("Bad Request");
            return ResponseEntity.status(400).cacheControl(CacheControl.noCache().mustRevalidate()).headers(headers).build();
        }
        try {
            jdbctemplate.queryForObject("Select 1", Integer.class);
            logger.info("Connected to Db,Healthz Check!");
            return ResponseEntity.status(200).cacheControl(CacheControl.noCache().mustRevalidate()).headers(headers).build();


        } catch (DataAccessException e) {
            logger.info("Serivce Unavailable");
            return ResponseEntity.status(503).cacheControl(CacheControl.noCache().mustRevalidate()).headers(headers).build();
        }
    }


    public boolean isDatabaseConnected() {
        try {
            jdbctemplate.queryForObject("SELECT 1", Integer.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}

