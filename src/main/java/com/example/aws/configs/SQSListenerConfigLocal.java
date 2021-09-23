package com.example.aws.configs;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.listener.QueueMessageHandler;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadMethodArgumentResolver;
import org.springframework.validation.Validator;

import java.util.Collections;

@Configuration
@Profile({"local"})
public class SQSListenerConfigLocal {

    @Bean
    SimpleMessageListenerContainer simpleMessageListenerContainer(final AmazonSQSAsync amazonSQSAsync,
                                                                  final QueueMessageHandler queueMessageHandler) {
        final SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setAmazonSqs(amazonSQSAsync);
        simpleMessageListenerContainer.setMessageHandler(queueMessageHandler);
        simpleMessageListenerContainer.setMaxNumberOfMessages(1);
        return simpleMessageListenerContainer;
    }

    @Bean
    AmazonSQSAsync amazonSQSAsync() {
        return AmazonSQSAsyncClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        "http://localhost:4576",
                        Regions.US_EAST_1.getName()
                ))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("accessKey", "secretKey")))
                .build();
    }

    @Bean
    QueueMessageHandler queueMessageHandler(final AmazonSQSAsync amazonSQSAsync,
                                            final MessageConverter messageConverter,
                                            final Validator validator) {
        final QueueMessageHandlerFactory queueMessageHandlerFactory = new QueueMessageHandlerFactory();
        final PayloadMethodArgumentResolver payloadMethodArgumentResolver = new PayloadMethodArgumentResolver(messageConverter, validator);
        queueMessageHandlerFactory.setArgumentResolvers(Collections.singletonList(payloadMethodArgumentResolver));
        queueMessageHandlerFactory.setAmazonSqs(amazonSQSAsync);
        queueMessageHandlerFactory.setMessageConverters(Collections.singletonList(messageConverter));
        return queueMessageHandlerFactory.createQueueMessageHandler();
    }

    @Bean
    MessageConverter messageConverter(final ObjectMapper mapper) {
        final MappingJackson2MessageConverter mappingJackson2MessageConverter = new MappingJackson2MessageConverter();
        mappingJackson2MessageConverter.setSerializedPayloadClass(String.class);
        mappingJackson2MessageConverter.setObjectMapper(mapper);
        return mappingJackson2MessageConverter;
    }

}
