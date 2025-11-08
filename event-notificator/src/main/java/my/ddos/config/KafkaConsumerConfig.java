package my.ddos.config;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import my.ddos.model.dto.EventBooking;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {
    @Bean
    public ConsumerFactory<String, EventBooking> consumerFactory(){
        JsonDeserializer<EventBooking> deserializer =
                new JsonDeserializer<>(EventBooking.class);
        deserializer.addTrustedPackages("*"); // доверенные пакеты
        deserializer.ignoreTypeHeaders();
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "booking-group");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EventBooking> kafkaListenerBookingContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, EventBooking> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}

