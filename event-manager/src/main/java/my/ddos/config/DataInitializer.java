package my.ddos.config;

import lombok.RequiredArgsConstructor;
import my.ddos.enums.UserRole;
import my.ddos.model.entity.Role;
import my.ddos.model.entity.User;
import my.ddos.repository.RoleRepository;
import my.ddos.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args){

        if(roleRepository.count() == 0){
            Role userRole = new Role(UserRole.ROLE_USER);
            Role adminRole = new Role(UserRole.ROLE_ADMIN);
            roleRepository.saveAll(List.of(userRole, adminRole));
        }
        if(userRepository.findByUsername("admin").isEmpty()){
            Role adminRole = roleRepository.findByRole(UserRole.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(UserRole.ROLE_ADMIN)));
            User admin = new User();
            admin.setUsername("admin");
            admin.setFullName("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.getUserRoles().add(adminRole);
            userRepository.save(admin);
        }
    }
}
