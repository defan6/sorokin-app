package my.ddos.controller.rest;


import lombok.RequiredArgsConstructor;
import my.ddos.model.dto.ChangeRoleRequest;
import my.ddos.model.dto.user.UserResponse;
import my.ddos.service.user.UserService;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getInfoAboutCurrentUser(Neo4jProperties.Authentication authentication){
        return ResponseEntity.ok(userService.getInfoAboutCurrentUser());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return ResponseEntity.ok(userService.getAll());
    }


    @PatchMapping("/change-role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> changeRole(@RequestBody ChangeRoleRequest changeRoleRequest){
        return ResponseEntity.ok(userService.changeRole(changeRoleRequest));
    }
}
