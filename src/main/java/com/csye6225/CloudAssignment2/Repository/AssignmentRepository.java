package com.csye6225.CloudAssignment2.Repository;

import com.csye6225.CloudAssignment2.Model.Assignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {
    boolean existsByName(String name);
    // You can add custom query methods here if needed
}
