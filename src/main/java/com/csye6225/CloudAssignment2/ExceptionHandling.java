package com.csye6225.CloudAssignment2;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandling {
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Void> exceptionhandler(HttpRequestMethodNotSupportedException exception){
        HttpHeaders headers;
        headers=new HttpHeaders();
        headers.set("Pragma", "no-cache");
        headers.set("X-Content-Type-Options","nosniff");
        return ResponseEntity.status(405).cacheControl(CacheControl.noCache().mustRevalidate()).headers(headers).build();
    }
}
