package com.example.aws.consumers.services;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.example.aws.consumers.controllers.dtos.response.MessageResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ConsumerService {

    @Value("${aws.arn}")
    private String arn;

    @Value("${aws.access_key_id}")
    private String accessKey;

    @Value("${aws.secret_access_key}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.queue}")
    private String queue;

    @Scheduled(fixedRate = 5000)
    public List<MessageResponseDTO> getMessage() {
        log.info("Verificando mensagens na fila");
        final List<MessageResponseDTO> messages = new ArrayList<>();
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        final AmazonSQS amazonSQS = AmazonSQSClientBuilder
                .standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        final List<Message> messageList = amazonSQS
                .receiveMessage(amazonSQS.getQueueUrl(queue).getQueueUrl())
                .getMessages();

        if (!messageList.isEmpty()) {
            for (Message body : messageList) {
                log.info(body.getBody());
                ObjectMapper mapper = new ObjectMapper();
                try {
                    messages.add(mapper.readValue(body.getBody(), MessageResponseDTO.class));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return messages;
    }

}
