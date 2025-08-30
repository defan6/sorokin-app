package my.ddos.controller.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ddos.model.dto.kafka.EventBooking;
import my.ddos.util.KafkaMessageConverter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import static my.ddos.config.kafka.KafkaTopicConfig.BOOKING_TOPIC;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaBookingProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendToBookingTopic(EventBooking eventBooking){
        Message<Object> kafkaMessage = buildMessage(eventBooking);
        kafkaTemplate.send(kafkaMessage);
        log.info("Sent message to topic {}: {}", BOOKING_TOPIC, eventBooking);
    }


    private Message<Object> buildMessage(Object object){
                return MessageBuilder.withPayload(object)
                        .setHeader(KafkaHeaders.TOPIC, BOOKING_TOPIC)
                        .build();
    }
}
