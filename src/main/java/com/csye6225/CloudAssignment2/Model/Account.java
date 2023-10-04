package com.csye6225.CloudAssignment2.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.sql.Date;


@Data
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Date accountCreated;
    private Date accountUpdated;


}
