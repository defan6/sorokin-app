package my.ddos.service.unit;

import my.ddos.controller.kafka.KafkaChangedRoleProducer;
import my.ddos.enums.UserRole;
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
import my.ddos.service.user.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private EventChangedRoleMapper eventChangedRoleMapper;

    @Mock
    private KafkaChangedRoleProducer kafkaChangedRoleProducer;


    @Mock
    private MessageService messageService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void save_shouldSaveUser_whenRoleExists() {
        // Given
        EventRegisterUser eventRegisterUser = new EventRegisterUser("testuser", "password", "test@test.com", "ROLE_USER");
        Role role = new Role();
        role.setRole(UserRole.ROLE_USER);
        User user = new User();

        when(roleRepository.findByRole(UserRole.ROLE_USER)).thenReturn(Optional.of(role));
        when(userMapper.toEntity(eventRegisterUser)).thenReturn(user);

        // When
        userService.save(eventRegisterUser);

        // Then
        verify(userRepository).save(user);
        assertThat(user.getUserRoles()).contains(role);
    }

    @Test
    void save_shouldThrowRoleNotFoundException_whenRoleDoesNotExist() {
        // Given
        EventRegisterUser eventRegisterUser = new EventRegisterUser("testuser", "password", "test@test.com", "ROLE_USER");

        when(roleRepository.findByRole(UserRole.ROLE_USER)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.save(eventRegisterUser))
                .isInstanceOf(RoleNotFoundException.class)
                .hasMessage("Role " + UserRole.ROLE_USER + " not found");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getInfoAboutCurrentUser_shouldReturnUserResponse_whenUserExists() {
        // Given
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        UserResponse expectedResponse = new UserResponse();
        expectedResponse.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(expectedResponse);

        // When
        UserResponse actualResponse = userService.getInfoAboutCurrentUser(username);

        // Then
        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(userRepository).findByUsername(username);
        verify(userMapper).toResponse(user);
    }

    @Test
    void getInfoAboutCurrentUser_shouldThrowException_whenUserDoesNotExist() {
        // Given
        String username = "nonexistent";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.getInfoAboutCurrentUser(username))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findByUsername(username);
        verify(userMapper, never()).toResponse(any(User.class));
    }

    @Test
    void getAll_shouldReturnListOfUserResponses_whenUsersExist() {
        // Given
        User user1 = new User();
        User user2 = new User();
        List<User> users = List.of(user1, user2);
        UserResponse response1 = new UserResponse();
        UserResponse response2 = new UserResponse();
        List<UserResponse> expectedResponses = List.of(response1, response2);

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toResponse(user1)).thenReturn(response1);
        when(userMapper.toResponse(user2)).thenReturn(response2);

        // When
        List<UserResponse> actualResponses = userService.getAll();

        // Then
        assertThat(actualResponses).isEqualTo(expectedResponses);
        verify(userRepository).findAll();
        verify(userMapper, times(2)).toResponse(any(User.class));
    }

    @Test
    void getAll_shouldReturnEmptyList_whenNoUsersExist() {
        // Given
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<UserResponse> actualResponses = userService.getAll();

        // Then
        assertThat(actualResponses).isEmpty();
        verify(userRepository).findAll();
        verify(userMapper, never()).toResponse(any(User.class));
    }

    @Test
    void changeRole_shouldChangeUserRole_whenUserAndRoleExist() {
        // Given
        String changedBy = "admin";
        long userId = 1L;
        String roleName = "ROLE_ADMIN";
        ChangeRoleRequest changeRoleRequest = new ChangeRoleRequest(userId, roleName);
        User user = new User();
        user.setId(userId);
        user.setUsername("testuser");
        Role role = new Role();
        role.setRole(UserRole.ROLE_ADMIN);
        UserResponse expectedResponse = new UserResponse();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findByRole(UserRole.ROLE_ADMIN)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(expectedResponse);
        when(eventChangedRoleMapper.toEventChangedRole(anyString(), anyString(), anyString()))
                .thenReturn(new my.ddos.event.EventChangedRole("testuser", changedBy, roleName));

        // When
        UserResponse actualResponse = userService.changeRole(changedBy, changeRoleRequest);

        // Then
        assertThat(actualResponse).isEqualTo(expectedResponse);
        assertThat(user.getUserRoles()).contains(role);
        verify(userRepository).findById(userId);
        verify(roleRepository).findByRole(UserRole.ROLE_ADMIN);
        verify(userRepository).save(user);
        verify(kafkaChangedRoleProducer).sendToChangedRoleTopic(any(my.ddos.event.EventChangedRole.class));
        verify(userMapper).toResponse(user);
    }

    @Test
    void changeRole_shouldThrowUserNotFoundException_whenUserNotFound() {
        // Given
        String changedBy = "admin";
        long userId = 1L;
        String roleName = "ROLE_ADMIN";
        ChangeRoleRequest changeRoleRequest = new ChangeRoleRequest(userId, roleName);

        when(messageService.getMessage(eq("user.not.found"), any())).thenReturn("User with id " + userId + " not found");
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.changeRole(changedBy, changeRoleRequest))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with id " + userId + " not found");

        verify(roleRepository, never()).findByRole(any(UserRole.class));
        verify(userRepository, never()).save(any(User.class));
        verify(kafkaChangedRoleProducer, never()).sendToChangedRoleTopic(any(my.ddos.event.EventChangedRole.class));
    }

    @Test
    void changeRole_shouldThrowRoleNotFoundException_whenRoleNotFound() {
        // Given
        String changedBy = "admin";
        long userId = 1L;
        String roleName = "ROLE_ADMIN";
        ChangeRoleRequest changeRoleRequest = new ChangeRoleRequest(userId, roleName);
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findByRole(UserRole.ROLE_ADMIN)).thenReturn(Optional.empty());
        when(messageService.getMessage(eq("role.not.found"), any())).thenReturn("Role not found");

        // When & Then
        assertThatThrownBy(() -> userService.changeRole(changedBy, changeRoleRequest))
                .isInstanceOf(RoleNotFoundException.class)
                .hasMessage("Role not found");

        verify(userRepository, never()).save(any(User.class));
        verify(kafkaChangedRoleProducer, never()).sendToChangedRoleTopic(any(my.ddos.event.EventChangedRole.class));
    }
}
