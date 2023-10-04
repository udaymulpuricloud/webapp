package com.csye6225.CloudAssignment2.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Date;


@Data
@Entity
public class Assignment {
    private String name;
    private int points;
    private int numOfAttempts;
    private Date deadline;
    private Date assignmentCreated;
    private Date assignmentUpdated;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

}
