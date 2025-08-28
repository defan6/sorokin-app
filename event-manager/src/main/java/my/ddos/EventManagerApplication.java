package my.ddos;

import lombok.RequiredArgsConstructor;
import my.ddos.enums.UserRole;
import my.ddos.model.dto.user.UserResponse;
import my.ddos.model.entity.Role;
import my.ddos.model.entity.User;
import my.ddos.repository.RoleRepository;
import my.ddos.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class EventManagerApplication {

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	private final PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(EventManagerApplication.class, args);
	}


}
