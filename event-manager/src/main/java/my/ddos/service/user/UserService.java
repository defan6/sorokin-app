package my.ddos.service.user;

import my.ddos.event.EventRegisterUser;
import my.ddos.model.dto.role.ChangeRoleRequest;
import my.ddos.model.dto.user.UserResponse;

import java.util.List;

public interface UserService {
    void save(EventRegisterUser eventRegisterUser);

    UserResponse getInfoAboutCurrentUser(String username);

    List<UserResponse> getAll();

    UserResponse changeRole(ChangeRoleRequest changeRoleRequest);
}
