package com.igrowker.wander.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3Config {

    private AmazonS3 amazonS3Client;

    @Value("${aws.s3.access-key}")
    private String accessKey;

    @Value("${aws.s3.secret-key}")
    private String secretKey;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.url}")
    private String url;

    public AmazonS3 getClient() {
        return amazonS3Client;
    }

    public String getUrl() {
        return url;
    }

    public String getBucketName() {
        return bucketName;
    }

    @PostConstruct
    private void init() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        this.amazonS3Client = AmazonS3ClientBuilder.standard()
            .withRegion(region)
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .build();
    }
}
