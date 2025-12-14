package my.ddos.controller.rest;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import my.ddos.model.dto.role.ChangeRoleRequest;
import my.ddos.model.dto.user.UserResponse;
import my.ddos.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getInfoAboutMe(@RequestHeader("X-Username") String username){
        return ResponseEntity.ok(userService.getInfoAboutCurrentUser(username));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return ResponseEntity.ok(userService.getAll());
    }


    @PatchMapping("/change-role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> changeRole(@RequestHeader("X-Username") String changedBy,
                                                   @RequestBody @Valid ChangeRoleRequest changeRoleRequest){
        return ResponseEntity.ok(userService.changeRole(changedBy, changeRoleRequest));
    }
}
