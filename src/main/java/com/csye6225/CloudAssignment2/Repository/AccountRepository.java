package com.csye6225.CloudAssignment2.Repository;

import com.csye6225.CloudAssignment2.Model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Account findByEmail(String email);

}
