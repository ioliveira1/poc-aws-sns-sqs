package com.example.aws.configs;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import com.amazonaws.services.sns.AmazonSNSClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile({"local"})
@Configuration
public class SNSClientConfigLocal {

    @Bean
    @Primary
    public AmazonSNSClient createClient() {

        final AWSCredentials awsCredentials = new BasicAWSCredentials("accessKey", "secretKey");
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
        AmazonSNSClient client = (AmazonSNSClient)AmazonSNSAsyncClientBuilder.standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                "http://localhost:4575",
                                Regions.US_EAST_1.getName()
                        )
                ).withCredentials(awsCredentialsProvider)
                .build();

        return client;
    }

}
