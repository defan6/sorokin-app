package my.ddos.config.kafka;

import my.ddos.event.EventRegisterUser;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.beans.BeanProperty;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {


    @Bean
    public ConsumerFactory<String, EventRegisterUser> consumerFactory(){
        JsonDeserializer<EventRegisterUser> deserializer =
                new JsonDeserializer<>(EventRegisterUser.class);
        deserializer.addTrustedPackages("*"); // доверенные пакеты
        deserializer.ignoreTypeHeaders();
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "register-user-group");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EventRegisterUser> kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, EventRegisterUser> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
