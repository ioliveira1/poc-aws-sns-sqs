package com.example.aws.consumers.services;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.Message;
import com.example.aws.consumers.controllers.dtos.responses.MessageResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ConsumerService {

    @Value("${aws.queue}")
    private String queue;

    private final AmazonSQSAsync amazonSQSAsync;

    public ConsumerService(final AmazonSQSAsync amazonSQSAsync) {
        this.amazonSQSAsync = amazonSQSAsync;
    }

    public List<MessageResponseDTO> getMessage() {
        log.info("Verificando mensagens na fila");
        final List<MessageResponseDTO> messages = new ArrayList<>();

        final List<Message> messageList = amazonSQSAsync
                .receiveMessage(amazonSQSAsync.getQueueUrl(queue).getQueueUrl())
                .getMessages();

        if (!messageList.isEmpty()) {
            for (Message body : messageList) {
                log.info(body.getBody());
                ObjectMapper mapper = new ObjectMapper();
                try {
                    messages.add(mapper.readValue(body.getBody(), MessageResponseDTO.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return messages;
    }

    @SqsListener("${aws.queue}")
    public void sqsListener(String message) {
        log.info("Message: {}", message);
    }

}
