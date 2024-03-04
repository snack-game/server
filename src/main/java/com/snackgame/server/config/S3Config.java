package com.snackgame.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

@Configuration
public class S3Config {

    @Profile("production")
    @Bean
    public AmazonS3 amazonS3OnInstance() {
        return AmazonS3Client.builder()
                .withRegion(Regions.AP_NORTHEAST_2)
                .withCredentials(InstanceProfileCredentialsProvider.getInstance())
                .build();
    }

    @Bean
    public AmazonS3 amazonS3(
            @Value("${cloud.aws.credentials.access-key}")
            String accessKey,
            @Value("${cloud.aws.credentials.secret-key}")
            String secretKey
    ) {
        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(accessKey, secretKey));
        return AmazonS3Client.builder()
                .withRegion(Regions.AP_NORTHEAST_2)
                .withCredentials(credentialsProvider)
                .build();
    }
}
