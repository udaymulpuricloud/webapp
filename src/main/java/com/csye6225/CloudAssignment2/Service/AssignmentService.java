package com.csye6225.CloudAssignment2.Service;

import com.csye6225.CloudAssignment2.Model.Assignment;
import com.csye6225.CloudAssignment2.Repository.AssignmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;
    public Assignment saveAssignment(Assignment assignment){
        assignment.setAssignmentCreated(Date.valueOf(LocalDate.now()));
        int points=assignment.getPoints();
        if(points < 1 || points > 10){
            throw new IllegalArgumentException("Points should be between 1 and 10");
        }
        String assignmentName = assignment.getName();
        if (assignmentName.contains(" ") || assignmentName.isEmpty()) {
            throw new IllegalArgumentException("Assignment name cannot be empty");
        }
        if (assignmentRepository.existsByName(assignmentName)) {
            throw new IllegalArgumentException("Assignment with the same name already exists");
        }
        return assignmentRepository.save(assignment);
    }

    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public Optional<Assignment> getAssignmentById(Long id) {
        return assignmentRepository.findById(id);
    }

    public void deleteAssignment(Long id) {
        Optional<Assignment> optionalAssignment = assignmentRepository.findById(id);

        if (optionalAssignment.isPresent()) {
            Assignment assignment = optionalAssignment.get();
            assignmentRepository.delete(assignment);
        } else {
            throw new EntityNotFoundException("Assignment not found with ID: " + id);
        }
    }
}
