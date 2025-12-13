package my.ddos;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
public class EventManagerApplication {
	public static void main(String[] args) {
		SpringApplication.run(EventManagerApplication.class, args);
	}


}
