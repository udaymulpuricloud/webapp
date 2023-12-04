package com.csye6225.CloudAssignment2.Controller;
import com.csye6225.CloudAssignment2.Model.Submission;
import com.csye6225.CloudAssignment2.Model.SubmissionRequest;
import com.csye6225.CloudAssignment2.Repository.AssignmentRepository;
import com.csye6225.CloudAssignment2.Repository.SubmissionRepository;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
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
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;



@RestController
@RequestMapping ("/v2/assignments")
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

    @Autowired
    private SubmissionRepository submissionRepository;
    private final StatsDClient statsDClient = new NonBlockingStatsDClient("metric","localhost",8125);

//    @Autowired
//    private StatsDClient statsDClient;

    Logger logger= LoggerFactory.getLogger("Assignment Controller");
    @PostMapping()
    public ResponseEntity<?> createAssignment(@RequestBody Assignment assignment){
        statsDClient.incrementCounter("Creating Assignment API");
        if(request.getQueryString()!= null){
            logger.warn("Received request with query string. Returning 400 Bad Request.");
            return ResponseEntity.status(400).cacheControl(CacheControl.noCache().mustRevalidate()).build();
        }
        try {
            assignment.setAssignmentUpdated(LocalDateTime.now());
            assignment.setAssignmentCreated(LocalDateTime.now());
            Assignment savedAssignment = assignmentService.saveAssignment(assignment);
            logger.info("Assignment Created Successfully!");
            return ResponseEntity.status(201).body(savedAssignment);
        } catch (IllegalArgumentException e) {
            logger.error("Error Creating the Assignment:"+e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/{id}/submission")
    public ResponseEntity<?> submitAssignment(@PathVariable UUID id, @RequestBody SubmissionRequest submissionRequest){
        statsDClient.incrementCounter("Submitted Assignment");

        try{
            Optional<Assignment> optionalAssignment=assignmentService.getAssignmentById(id);
            UUID submitbyid = (UUID) request.getSession().getAttribute("accountId");
            String submitbymail=accountService.findById(submitbyid).getEmail();
            if(optionalAssignment.isPresent()){
                Assignment assignment=optionalAssignment.get();
//                if(submitid.equals(assignment.getCreatedBy().getId())) {
                    if (AssignmentService.isAssignmentOpen(assignment)) {
                        if(request.getQueryString()!=null){
                            logger.warn("Recevied request with query String. Returning 400 bad request");
                            return ResponseEntity.status(400).cacheControl(CacheControl.noCache().mustRevalidate()).build();
                        }
                        int maxRetries = assignment.getNum_of_attempts();
                        int submissionAttempts =submissionRepository.getSubmissionAttempts(id,submitbymail);

                        if (submissionAttempts < maxRetries) {
                            Submission submission = assignmentService.processSubmission(assignment, submissionRequest);
                            logger.info("Assignment submitted successfully!");
                            return ResponseEntity.status(201).body(submission);
                        } else {
                            logger.warn("User has exceeded maximum submission retries.");
                            return ResponseEntity.status(403).body("Maximum number of submissions crosses!");
                        }

                    }
                    else
                    {
                        logger.warn("Assignment is not open for submission.");
                        return ResponseEntity.status(403).body("Assignment Deadline Crossed , unable to submit");
                    }
//                    }
//                else {
//                    return ResponseEntity.status(401).cacheControl(CacheControl.noCache().mustRevalidate()).build();
//
//                }
                }
            else {
                logger.warn("Assignment not found. Returning 404 Not Found.");
                return ResponseEntity.status(404).build();
            }


        }
        catch(IllegalArgumentException e) {
            logger.error("Error submitting the assignment: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping

    public ResponseEntity<Object> getAssignments(@RequestBody(required = false) Object body) {
        statsDClient.incrementCounter("Get Assignment API");
        if(body!=null || request.getQueryString()!= null){
            logger.warn("Received request with query string. Returning 400 Bad Request.");
            return ResponseEntity.status(400).cacheControl(CacheControl.noCache().mustRevalidate()).build();
        }
        logger.info("Retrieved assignments successfully.");

        return ResponseEntity.ok(assignmentService.getAllAssignments());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Assignment> getAssignmentById(@PathVariable UUID id,@RequestBody(required = false) Object body) {
        statsDClient.incrementCounter("Get Assignment By ID API");
        if(body!=null || request.getQueryString()!= null){
            logger.warn("Received request with body or query string. Returning 400 Bad Request.");
            return ResponseEntity.status(400).cacheControl(CacheControl.noCache().mustRevalidate()).build();
        }

   
        Optional<Assignment> assignment = assignmentService.getAssignmentById(id);

        if (assignment.isPresent()) {
            logger.info("Retrieved assignment with ID " + id + " successfully.");
            return ResponseEntity.ok(assignment.get());
        } else {
            logger.warn("Assignment with ID " + id + " not found.");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAssignment(@PathVariable UUID id, @RequestBody(required = false) Assignment updatedAssignment) {
        statsDClient.incrementCounter("Update Assignment API");
        if(updatedAssignment==null || request.getQueryString()!= null){
            logger.warn("Received request with null body or query string. Returning 400 Bad Request.");
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
                    logger.info("Assignment with ID " + id + " updated successfully.");
                    return ResponseEntity.status(204).build();
                }catch (Exception ex){
                    logger.error("Error updating assignment with ID " + id + ": " + ex.getMessage());
                    return ResponseEntity.status(400).body(ex.getMessage());
                }
            }
            else {
                logger.warn("Unauthorized access to update assignment with ID " + id);
                return  ResponseEntity.status(403).build();

            }
        } else {
            logger.warn("Assignment with ID " + id + " not found for update.");
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAssignment(@PathVariable UUID id,@RequestBody(required = false) Object body) {
        statsDClient.incrementCounter("Delete Assignment API");
        if(body!=null || request.getQueryString()!= null){
            logger.warn("Received request with null body or query string. Returning 400 Bad Request.");
            return ResponseEntity.status(400).cacheControl(CacheControl.noCache().mustRevalidate()).build();
        }
        Optional<Assignment> existingAssignment = assignmentService.getAssignmentById(id);
        UUID deleteid = (UUID) request.getSession().getAttribute("accountId");
        String emaildel=accountService.findById(deleteid).getEmail();
        if (existingAssignment.isPresent()) {
            Assignment assignment1=existingAssignment.get();

                if (deleteid.equals(assignment1.getCreatedBy().getId())) {
                    int submissionAttempts = submissionRepository.getsubmissionsoverall(id);
                    if (submissionAttempts > 0) {
                        logger.warn("Cannot delete assignment with ID " + id + " because submissions are present.");
                        return ResponseEntity.status(400).body("Cannot delete assignment with existing submissions.");
                    }
                    else {
                        assignmentService.deleteAssignment(id);
                        logger.info("Assignment with ID " + id + " deleted successfully.");
                        return ResponseEntity.status(204).build();
                    }
                } else {
                    logger.warn("Unauthorized access to delete assignment with ID " + id);
                    return ResponseEntity.status(403).build();
                }

        } else {
            logger.warn("Assignment with ID " + id + " not found for deletion.");
            return ResponseEntity.notFound().build();
        }
    }
}


