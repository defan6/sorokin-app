package my.ddos.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class KafkaMessageConverter {

    private final ObjectMapper mapper;


    public String toJson(Object o){
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing object " + o + " to string");
        }

    }


    public <T> T toObject(String message, Class<T> tClass){
        try {
            return mapper.readValue(message, tClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing message " + message + " to object" );
        }
    }
}
