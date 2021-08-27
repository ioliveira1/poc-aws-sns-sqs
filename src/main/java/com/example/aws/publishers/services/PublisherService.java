package com.example.aws.publishers.services;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.example.aws.publishers.controllers.dtos.request.MessageRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {

    @Value("${aws.arn}")
    private String arn;

    @Value("${aws.access_key_id}")
    private String accessKey;

    @Value("${aws.secret_access_key}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

    public void postMessage(MessageRequestDTO messageRequestDTO) {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        final AmazonSNSClient snsClient = (AmazonSNSClient) AmazonSNSClientBuilder
                .standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        snsClient.publish(arn, messageRequestDTO.getBody(), messageRequestDTO.getSubject());
    }

}
