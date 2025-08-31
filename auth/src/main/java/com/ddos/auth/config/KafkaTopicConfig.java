package com.ddos.auth.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.beans.JavaBean;

@Configuration
public class KafkaTopicConfig {

    public static final String REGISTER_USER_TOPIC = "register-user-topic";

    @Bean
    NewTopic createRegisterUserTopic(){
        return TopicBuilder.name(REGISTER_USER_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
