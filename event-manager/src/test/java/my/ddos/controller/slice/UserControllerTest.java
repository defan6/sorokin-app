package my.ddos.controller.slice;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.ddos.controller.rest.UserController;
import my.ddos.model.dto.role.ChangeRoleRequest;
import my.ddos.model.dto.user.UserResponse;
import my.ddos.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    void getInfoAboutMe() throws Exception {
        // Given
        String username = "testuser";
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(username);
        when(userService.getInfoAboutCurrentUser(username)).thenReturn(userResponse);

        // When & Then
        mockMvc.perform(get("/api/manager/users/me")
                        .header("X-Username", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username));
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() throws Exception {
        // Given
        when(userService.getAll()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/manager/users/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    void changeRole_shouldChangeUserRole() throws Exception {
        // Given
        String changedBy = "admin";
        ChangeRoleRequest changeRoleRequest = new ChangeRoleRequest(1L, "ROLE_ADMIN");
        UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        when(userService.changeRole(eq(changedBy), any(ChangeRoleRequest.class))).thenReturn(userResponse);

        // When & Then
        mockMvc.perform(patch("/api/manager/users/admin/change-role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Username", changedBy)
                        .content(objectMapper.writeValueAsString(changeRoleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }
}
