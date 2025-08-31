package com.ddos.auth.kafka.controller;

import com.ddos.auth.kafka.event.EventRegisterUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import static com.ddos.auth.config.KafkaTopicConfig.REGISTER_USER_TOPIC;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaRegisterProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;


    public void sendMessageToRegisterTopic(EventRegisterUser eventRegisterUser){
        Message<EventRegisterUser> eventRegisterUserMessage = buildMessage(eventRegisterUser);
        kafkaTemplate.send(eventRegisterUserMessage);
        log.info("Sent message {} to {}", eventRegisterUserMessage, REGISTER_USER_TOPIC);

    }

    private Message<EventRegisterUser> buildMessage(EventRegisterUser eventRegisterUser){
        return MessageBuilder.withPayload(eventRegisterUser)
                .setHeader(KafkaHeaders.TOPIC, REGISTER_USER_TOPIC)
                .build();

    }
}
