package com.ddos.auth.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String BOOKING_TOPIC = "booking-topic";


    public static final String CHANGE_EVENT_INFO_TOPIC = "change-event-topic";

    public static final String CHANGE_USER_ROLE_TOPIC = "change-user-role-topic";


    public static final String REGISTER_USER_TOPIC = "register-user-topic";

    @Bean
    NewTopic createRegisterUserTopic(){
        return TopicBuilder.name(REGISTER_USER_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
