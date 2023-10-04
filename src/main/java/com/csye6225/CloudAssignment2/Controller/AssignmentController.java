package com.csye6225.CloudAssignment2.Controller;

import com.csye6225.CloudAssignment2.Model.Assignment;

import com.csye6225.CloudAssignment2.Service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping ("/v1/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;
    @PostMapping()
    public Assignment createAssignment(@RequestBody Assignment assignment){

      return assignmentService.saveAssignment(assignment);

    }
    @GetMapping
    public List<Assignment> getAssignments() {
        return assignmentService.getAllAssignments();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Assignment> getAssignmentById(@PathVariable Long id) {
        Optional<Assignment> assignment = assignmentService.getAssignmentById(id);

        if (assignment.isPresent()) {
            return ResponseEntity.ok(assignment.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Assignment> updateAssignment(@PathVariable Long id, @RequestBody Assignment updatedAssignment) {
        Optional<Assignment> existingAssignment = assignmentService.getAssignmentById(id);

        if (existingAssignment.isPresent()) {
            Assignment assignment = existingAssignment.get();
            assignment.setName(updatedAssignment.getName());
            assignment.setPoints(updatedAssignment.getPoints());
            assignment.setNumOfAttempts(updatedAssignment.getNumOfAttempts());
            assignment.setDeadline(updatedAssignment.getDeadline());

            Assignment updated = assignmentService.saveAssignment(assignment);
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        Optional<Assignment> existingAssignment = assignmentService.getAssignmentById(id);

        if (existingAssignment.isPresent()) {
            assignmentService.deleteAssignment(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}


