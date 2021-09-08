package com.example.aws.publishers.services;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.example.aws.publishers.controllers.dtos.requests.MessageRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {

    @Value("${aws.arn}")
    private String arn;

    @Autowired
    private AmazonSNSClient client;

    public void postMessage(MessageRequestDTO messageRequestDTO) {
        client.publish(arn, messageRequestDTO.getBody(), messageRequestDTO.getSubject());
    }

}
