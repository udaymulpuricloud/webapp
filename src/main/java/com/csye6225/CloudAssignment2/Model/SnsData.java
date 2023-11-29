package com.csye6225.CloudAssignment2.Model;

import lombok.Data;

import java.util.UUID;

@Data
public class SnsData {
    private String assignmentId;

    private UUID submissionId;

    private String submissionUrl;

    private String submissionDate;
    private String emailId;

    private String firstName;

    private String assignmentName;

}
