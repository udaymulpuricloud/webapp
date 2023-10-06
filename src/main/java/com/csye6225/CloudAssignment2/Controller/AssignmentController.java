package com.csye6225.CloudAssignment2.Controller;
import com.csye6225.CloudAssignment2.Repository.AssignmentRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import com.csye6225.CloudAssignment2.Model.Account;
import com.csye6225.CloudAssignment2.Model.Assignment;


import com.csye6225.CloudAssignment2.Repository.AccountRepository;
import com.csye6225.CloudAssignment2.Service.AccountService;
import com.csye6225.CloudAssignment2.Service.AssignmentService;
import jdk.jfr.Frequency;
import org.apache.coyote.Request;
import org.apache.tomcat.util.http.parser.Authorization;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;



@RestController
@RequestMapping ("/v1/assignments")
public class AssignmentController {

//    private HttpServletRequest request;

    @Autowired
    HttpServletRequest request;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private AccountService accountService;

    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    private AccountRepository accountRepository;
    @PostMapping()
    public ResponseEntity<?> createAssignment(@RequestBody Assignment assignment){
        if(request.getQueryString()!= null){
            return ResponseEntity.status(400).cacheControl(CacheControl.noCache().mustRevalidate()).build();
        }
        try {
            assignment.setAssignmentUpdated(LocalDateTime.now());
            assignment.setAssignmentCreated(LocalDateTime.now());
            Assignment savedAssignment = assignmentService.saveAssignment(assignment);
            return ResponseEntity.status(201).body(savedAssignment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping

    public ResponseEntity<Object> getAssignments(@RequestBody(required = false) Object body) {
        if(body!=null || request.getQueryString()!= null){
            return ResponseEntity.status(400).cacheControl(CacheControl.noCache().mustRevalidate()).build();
        }

        return ResponseEntity.ok(assignmentService.getAllAssignments());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Assignment> getAssignmentById(@PathVariable UUID id,@RequestBody(required = false) Object body) {
        if(body!=null || request.getQueryString()!= null){
            return ResponseEntity.status(400).cacheControl(CacheControl.noCache().mustRevalidate()).build();
        }

   
        Optional<Assignment> assignment = assignmentService.getAssignmentById(id);

        if (assignment.isPresent()) {
            return ResponseEntity.ok(assignment.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAssignment(@PathVariable UUID id, @RequestBody(required = false) Assignment updatedAssignment) {
        if(updatedAssignment==null || request.getQueryString()!= null){
            return ResponseEntity.status(400).cacheControl(CacheControl.noCache().mustRevalidate()).build();
        }

        Optional<Assignment> existingAssignment = assignmentService.getAssignmentById(id);

        String assignmentName = updatedAssignment.getName();
        if (assignmentName.isEmpty() || assignmentName.isEmpty()||assignmentName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("The name of Assignment shouldn't be empty");
        }

        UUID gettingId = (UUID) request.getSession().getAttribute("accountId");


        if (existingAssignment.isPresent()) {

            Assignment assignment = existingAssignment.get();
            if (gettingId.equals(assignment.getCreatedBy().getId())) {
                assignment.setName(updatedAssignment.getName());
                assignment.setPoints(updatedAssignment.getPoints());
                assignment.setNum_of_attempts(updatedAssignment.getNum_of_attempts());
                assignment.setDeadline(updatedAssignment.getDeadline());
                assignment.setAssignmentUpdated(LocalDateTime.now());
                try{
                    assignmentService.saveAssignment(assignment);
                    return ResponseEntity.status(204).build();
                }catch (Exception ex){
                    return ResponseEntity.status(400).body(ex.getMessage());
                }
            }
            else {
                return  ResponseEntity.status(403).build();

            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAssignment(@PathVariable UUID id,@RequestBody(required = false) Object body) {
        if(body!=null || request.getQueryString()!= null){
            return ResponseEntity.status(400).cacheControl(CacheControl.noCache().mustRevalidate()).build();
        }
        Optional<Assignment> existingAssignment = assignmentService.getAssignmentById(id);
        UUID deleteid = (UUID) request.getSession().getAttribute("accountId");
        if (existingAssignment.isPresent()) {
            Assignment assignment1=existingAssignment.get();
            if(deleteid.equals(assignment1.getCreatedBy().getId())) {
                assignmentService.deleteAssignment(id);
                return ResponseEntity.status(204).build();

            }
            else{
                return ResponseEntity.status(403).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}


