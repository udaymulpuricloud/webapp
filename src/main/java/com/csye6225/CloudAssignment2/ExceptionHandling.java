package com.csye6225.CloudAssignment2;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandling extends ResponseEntityExceptionHandler {
//    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
//    public ResponseEntity<Void> exceptionhandler(HttpRequestMethodNotSupportedException exception){
//        HttpHeaders headers;
//        headers=new HttpHeaders();
//        headers.set("Pragma", "no-cache");
//        headers.set("X-Content-Type-Options","nosniff");
//        return ResponseEntity.status(405).cacheControl(CacheControl.noCache().mustRevalidate()).headers(headers).build();
//    }
//
//    @ExceptionHandler(NoHandlerFoundException.class)
//    public ResponseEntity<Void> exceptionhandle(NoHandlerFoundException exception){
//        HttpHeaders head;
//        head=new HttpHeaders();
//        head.set("Pragma", "no-cache");
//        head.set("X-Content-Type-Options","nosniff");
//        return ResponseEntity.status(404).cacheControl(CacheControl.noCache().mustRevalidate()).headers(head).build();
//    }

    @Override
    public ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                      HttpHeaders headers, HttpStatusCode status,
                                                                      WebRequest request) {
        headers.set("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.set("Pragma", "no-cache");
        headers.set("X-Content-Type-Options", "nosniff");
        return ResponseEntity.status(405).headers(headers).build();
    }

    @Override
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException e,
                                                                HttpHeaders headers, HttpStatusCode status,
                                                                WebRequest request) {
        return ResponseEntity.status(404).headers(headers).build();
    }

}
