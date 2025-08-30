package my.ddos.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String BOOKING_TOPIC = "booking-topic";


    public static final String CHANGE_EVENT_INFO_TOPIC = "change-event-topic";


    @Bean
    public NewTopic createBookingTopic(){
        return TopicBuilder.name(BOOKING_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }


    @Bean
    public NewTopic createChangeEventInfoTopic(){
        return TopicBuilder.name(CHANGE_EVENT_INFO_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
