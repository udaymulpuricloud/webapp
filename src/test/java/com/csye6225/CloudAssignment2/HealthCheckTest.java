package com.csye6225.CloudAssignment2;

import com.csye6225.CloudAssignment2.Controller.HealthzController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HealthCheckTest {
    @Autowired
    private HealthzController healthzController;
    @Test
    public void testDataBaseConnection() {
        assert healthzController.isDatabaseConnected();
    }
}