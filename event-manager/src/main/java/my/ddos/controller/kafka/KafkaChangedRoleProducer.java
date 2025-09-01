package my.ddos.controller.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ddos.event.EventChangedRole;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import static my.ddos.config.kafka.KafkaTopicConfig.CHANGE_EVENT_INFO_TOPIC;
import static my.ddos.config.kafka.KafkaTopicConfig.CHANGE_USER_ROLE_TOPIC;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaChangedRoleProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;


    public void sendToChangedRoleTopic(EventChangedRole message){
        Message<Object> kafkaMessage = buildMessage(message);
        kafkaTemplate.send(kafkaMessage);
        log.info("Sent message to topic {}: {}", CHANGE_EVENT_INFO_TOPIC, kafkaMessage);
    }

    private static Message<Object> buildMessage(Object message){
        return MessageBuilder.withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, CHANGE_USER_ROLE_TOPIC)
                .build();
    }
}
