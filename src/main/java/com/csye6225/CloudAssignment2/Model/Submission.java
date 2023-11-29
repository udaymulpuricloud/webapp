package com.csye6225.CloudAssignment2.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Component
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID assignmentid;
    private String submissionurl;

   private String submittedby;


    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime submission_date;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime submission_updated;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAssignmentid() {
        return assignmentid;
    }

    public void setAssignmentid(UUID assignmentid) {
        this.assignmentid = assignmentid;
    }

    public String getSubmissionurl() {
        return submissionurl;
    }

    public void setSubmissionurl(String submissionurl) {
        this.submissionurl = submissionurl;
    }

    public LocalDateTime getSubmission_date() {
        return submission_date;
    }

    public void setSubmission_date(LocalDateTime submission_date) {
        this.submission_date = submission_date;
    }

    public LocalDateTime getSubmission_updated() {
        return submission_updated;
    }

    public void setSubmission_updated(LocalDateTime submission_updated) {
        this.submission_updated = submission_updated;
    }

    public void setSubmittedby(String submittedby) {
        this.submittedby = submittedby;
    }

    public String getSubmittedby() {
        return submittedby;
    }
}
