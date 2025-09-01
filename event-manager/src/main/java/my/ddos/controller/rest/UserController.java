package my.ddos.controller.rest;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import my.ddos.model.dto.role.ChangeRoleRequest;
import my.ddos.model.dto.user.UserResponse;
import my.ddos.service.user.UserService;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getInfoAboutCurrentUser(@RequestHeader("X-Username") String username){
        return ResponseEntity.ok(userService.getInfoAboutCurrentUser(username));
    }

    @GetMapping("/admin")
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return ResponseEntity.ok(userService.getAll());
    }


    @PatchMapping("admin/change-role")
    public ResponseEntity<UserResponse> changeRole(@RequestBody ChangeRoleRequest changeRoleRequest){
        return ResponseEntity.ok(userService.changeRole(changeRoleRequest));
    }
}
