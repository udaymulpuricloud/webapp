package com.csye6225.CloudAssignment2.Service;

import com.csye6225.CloudAssignment2.Model.Assignment;
import com.csye6225.CloudAssignment2.Model.SnsData;
import com.csye6225.CloudAssignment2.Model.Submission;
import com.csye6225.CloudAssignment2.Model.SubmissionRequest;
import com.csye6225.CloudAssignment2.Repository.AssignmentRepository;
//import com.csye6225.CloudAssignment2.Repository.SubmissionRepository;
import com.csye6225.CloudAssignment2.Repository.SubmissionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreateTopicRequest;
import software.amazon.awssdk.services.sns.model.CreateTopicResponse;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.SnsException;




@Service
public class AssignmentService {


    @Autowired
    HttpServletRequest request;
    @Autowired
    AccountService accountService;
    @Autowired
    Submission submission;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private SubmissionRepository submissionRepository;


    @Autowired
    private SnsClient snsClient;

    @Value("${TopicARN}")
    private  String topicarn;


    public Assignment saveAssignment(Assignment assignment) {



        int points=assignment.getPoints();
        if(points < 1 || points > 10){
            throw new IllegalArgumentException("Points should be between 1 and 10");
        }
        int numAttempts = assignment.getNum_of_attempts();
        if(numAttempts < 1 || numAttempts > 10){
            throw new IllegalArgumentException("Number of attempts should be between 1 and 10");
        }
        if(!assignment.getName().matches(".*[a-zA-Z].*")){
            throw new IllegalArgumentException("Assignment name should contain atleast one Alphabet");
        }

        String assignmentName = assignment.getName();
        if (assignmentName.isEmpty() || assignmentName.isEmpty()||assignmentName.trim().isEmpty()) {
            throw new IllegalArgumentException("Assignment name cannot be empty");
        }

        UUID id = (UUID) request.getSession().getAttribute("accountId");
        assignment.setCreatedBy(accountService.findById(id));

        return assignmentRepository.save(assignment);
    }

    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public Optional<Assignment> getAssignmentById(UUID id) {
        return assignmentRepository.findById(id);
    }

    public void deleteAssignment(UUID id) {
        Optional<Assignment> optionalAssignment = assignmentRepository.findById(id);

        if (optionalAssignment.isPresent()) {
            Assignment assignment = optionalAssignment.get();
            assignmentRepository.delete(assignment);
        } else {
            throw new EntityNotFoundException("Assignment not found with ID: " + id);
        }
    }
    public static boolean isAssignmentOpen(Assignment assignment){
        LocalDateTime now = LocalDateTime.now();
        return now.isBefore(assignment.getDeadline());
    }

    public Submission processSubmission(Assignment assignment, SubmissionRequest submissionRequest){
        UUID gettingIdd = (UUID) request.getSession().getAttribute("accountId");
        String emailid=accountService.findById(gettingIdd).getEmail();

     Submission submission = new Submission();
     submission.setId(UUID.randomUUID());
     submission.setSubmittedby(emailid);
     submission.setAssignmentid(assignment.getId());
     submission.setSubmission_updated(LocalDateTime.now());
     submission.setSubmission_date(LocalDateTime.now());
     submission.setSubmissionurl(submissionRequest.getSubmission_url());


     sendSMS(assignment,submission);

       return submissionRepository.save(submission);
// return submission;

    }

    private void sendSMS(Assignment assignment, Submission submission) {
        UUID gettingId = (UUID) request.getSession().getAttribute("accountId");

        String email = accountService.findById(gettingId).getEmail();
        String fname= accountService.findById(gettingId).getFirstname();
        SnsData snsData = new SnsData();
        snsData.setSubmissionDate(submission.toString());
        snsData.setAssignmentId(assignment.getId().toString());
        snsData.setSubmissionId(submission.getId());
        snsData.setSubmissionUrl(submission.getSubmissionurl());
        snsData.setEmailId(email);
        snsData.setFirstName(fname);
        snsData.setAssignmentName(assignment.getName());
        ObjectMapper objectMapper = new ObjectMapper();

        String message= "";
        try {
            message = objectMapper.writeValueAsString(snsData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        PublishRequest publishRequest= PublishRequest.builder()
                .topicArn(topicarn)
                .message(message)
                .build();
        snsClient.publish(publishRequest);

    }


}
