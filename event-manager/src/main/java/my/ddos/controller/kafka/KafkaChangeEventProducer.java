package my.ddos.controller.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ddos.model.dto.kafka.EventChangedEvent;
import my.ddos.util.KafkaMessageConverter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import static my.ddos.config.kafka.KafkaTopicConfig.CHANGE_EVENT_INFO_TOPIC;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaChangeEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendToChangeEventTopic(EventChangedEvent eventChangedEvent){
        Message<Object> kafkaMessage = buildMessage(eventChangedEvent);
        kafkaTemplate.send(kafkaMessage);
        log.info("Sent message to topic {}: {}", CHANGE_EVENT_INFO_TOPIC, kafkaMessage);
    }


    private static Message<Object> buildMessage(Object message){
        return MessageBuilder.withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, CHANGE_EVENT_INFO_TOPIC)
                .build();
    }
}
