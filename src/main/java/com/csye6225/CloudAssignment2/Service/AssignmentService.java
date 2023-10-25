package com.csye6225.CloudAssignment2.Service;

import com.csye6225.CloudAssignment2.Model.Assignment;
import com.csye6225.CloudAssignment2.Repository.AccountRepository;
import com.csye6225.CloudAssignment2.Repository.AssignmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class AssignmentService {


    @Autowired
    HttpServletRequest request;
    @Autowired
    AccountService accountService;

    @Autowired
    private AssignmentRepository assignmentRepository;
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
}
