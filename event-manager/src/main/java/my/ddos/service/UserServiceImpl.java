package my.ddos.service;

import lombok.RequiredArgsConstructor;
import my.ddos.enums.UserRole;
import my.ddos.model.dto.register.RegisterRequest;
import my.ddos.model.dto.register.RegisterResponse;
import my.ddos.model.entity.Role;
import my.ddos.model.entity.User;
import my.ddos.repository.RoleRepository;
import my.ddos.repository.UserRepository;
import org.hibernate.bytecode.internal.bytebuddy.PassThroughInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    @Value("${success.register.message}")
    String successRegisterMessage;

    @Override
    public RegisterResponse save(RegisterRequest registerRequest) {
        Role role = new Role(UserRole.USER_ROLE);
        roleRepository.save(role);
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setFullName(registerRequest.getFullName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.getUserRoles().add(role);
        role.getUsers().add(user);

        userRepository.save(user);
        return new RegisterResponse(user.getUsername(), successRegisterMessage);

    }
}
