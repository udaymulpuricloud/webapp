package com.csye6225.CloudAssignment2.Repository;

import com.csye6225.CloudAssignment2.Model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, UUID> {
    @Query("SELECT COUNT(*) FROM Submission s WHERE s.assignmentid= :assignmentId")
    int getSubmissionAttempts(@Param("assignmentId") UUID assignmentId);
}
