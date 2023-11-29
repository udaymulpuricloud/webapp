package com.csye6225.CloudAssignment2.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;


@Configuration
public class AppConfig {
    @Bean
    public SnsClient snsClient(){
        AwsCredentialsProvider awsCredentialsProvider= ProfileCredentialsProvider.builder().profileName("demo").build();
        return SnsClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(awsCredentialsProvider)
                .build();
    }
}
