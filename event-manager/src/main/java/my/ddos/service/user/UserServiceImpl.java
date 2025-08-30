package my.ddos.service.user;

import lombok.RequiredArgsConstructor;
import my.ddos.enums.UserRole;
import my.ddos.exception.RoleNotFoundException;
import my.ddos.exception.UserNotFoundException;
import my.ddos.mapper.UserMapper;
import my.ddos.model.dto.role.ChangeRoleRequest;
import my.ddos.model.dto.register.RegisterRequest;
import my.ddos.model.dto.register.RegisterResponse;
import my.ddos.model.dto.user.UserResponse;
import my.ddos.model.entity.Role;
import my.ddos.model.entity.User;
import my.ddos.repository.RoleRepository;
import my.ddos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    @Value("${success.register.message}")
    String successRegisterMessage;

    @Override
    @Transactional
    public RegisterResponse save(RegisterRequest registerRequest) {
        Role userRole = roleRepository.findByRole(UserRole.ROLE_USER).orElseThrow(()-> new RoleNotFoundException("Role ROLE_USER not found"));
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setFullName(registerRequest.getFullName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.getUserRoles().add(userRole);
        userRepository.save(user);
        return new RegisterResponse(user.getUsername(), successRegisterMessage);

    }

    @Override
    public UserResponse getInfoAboutCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User with username " + username + " not found."));
        return userMapper.toResponse(currentUser);
    }

    @Override
    public List<UserResponse> getAll() {
        return userRepository.findAll().stream().map(userMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public UserResponse changeRole(ChangeRoleRequest changeRoleRequest) {
        User user = userRepository.findById(changeRoleRequest.getId()).orElseThrow(()-> new UserNotFoundException("User with id " + changeRoleRequest.getId() + " not found"));
        UserRole role = UserRole.fromString(changeRoleRequest.getRole());
        Role userRole = roleRepository.findByRole(role).orElseThrow(() -> new RoleNotFoundException("Role " + role
                + " not found"));
        user.getUserRoles().add(userRole);
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }
}
