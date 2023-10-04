package com.csye6225.CloudAssignment2.Service;

import com.csye6225.CloudAssignment2.Model.Assignment;
import com.csye6225.CloudAssignment2.Repository.AssignmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
