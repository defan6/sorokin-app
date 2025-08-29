package my.ddos.service.user;

import my.ddos.model.dto.ChangeRoleRequest;
import my.ddos.model.dto.register.RegisterRequest;
import my.ddos.model.dto.register.RegisterResponse;
import my.ddos.model.dto.user.UserResponse;

import java.util.List;

public interface UserService {
    RegisterResponse save(RegisterRequest registerRequest);

    UserResponse getInfoAboutCurrentUser();

    List<UserResponse> getAll();

    UserResponse changeRole(ChangeRoleRequest changeRoleRequest);
}
