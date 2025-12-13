package my.ddos.service.user;

import lombok.RequiredArgsConstructor;
import my.ddos.controller.kafka.KafkaChangedRoleProducer;
import my.ddos.enums.UserRole;
import my.ddos.event.EventChangedRole;
import my.ddos.event.EventRegisterUser;
import my.ddos.exception.RoleNotFoundException;
import my.ddos.exception.UserNotFoundException;
import my.ddos.mapper.EventChangedRoleMapper;
import my.ddos.mapper.UserMapper;
import my.ddos.model.dto.role.ChangeRoleRequest;
import my.ddos.model.dto.user.UserResponse;
import my.ddos.model.entity.Role;
import my.ddos.model.entity.User;
import my.ddos.repository.RoleRepository;
import my.ddos.repository.UserRepository;
import my.ddos.service.i18n.MessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    private final EventChangedRoleMapper eventChangedRoleMapper;

    private final KafkaChangedRoleProducer kafkaChangedRoleProducer;

    private final MessageService messageService;


    @Override
    @Transactional
    public void save(EventRegisterUser eventRegisterUser) {
        UserRole userRole = UserRole.fromString(eventRegisterUser.role());
        Role role = roleRepository.findByRole(userRole)
                .orElseThrow(()-> new RoleNotFoundException("Role " + userRole +  " not found"));
        User user = userMapper.toEntity(eventRegisterUser);
        user.getUserRoles().add(role);
        userRepository.save(user);
    }

    @Override
    public UserResponse getInfoAboutCurrentUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        return userMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> getAll() {
        return userRepository.findAll().stream().map(userMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public UserResponse changeRole(String changedBy, ChangeRoleRequest changeRoleRequest) {
        User user = userRepository.findById(changeRoleRequest.getId())
                .orElseThrow(() -> new UserNotFoundException(messageService.getMessage
                        ("user.not.found", new Object[]{changeRoleRequest.getId()})));
        UserRole role = UserRole.fromString(changeRoleRequest.getRole());
        Role userRole = roleRepository.findByRole(role).orElseThrow(() -> new RoleNotFoundException
                (messageService.getMessage("role.not.found", new Object[]{role})));
        user.getUserRoles().clear();
        user.getUserRoles().add(userRole);
        User savedUser = userRepository.save(user);
        EventChangedRole eventChangedRole = eventChangedRoleMapper
                .toEventChangedRole(user.getUsername(), changedBy, role.name());
        kafkaChangedRoleProducer.sendToChangedRoleTopic(eventChangedRole);
        return userMapper.toResponse(savedUser);
    }
}
