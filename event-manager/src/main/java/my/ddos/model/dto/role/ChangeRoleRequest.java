package my.ddos.model.dto.role;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeRoleRequest {
    @NotNull
    private Long id;

    @NotNull
    private String role;
}
