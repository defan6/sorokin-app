package my.ddos.model.dto.venue;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VenueRequest {
    @NotBlank(message = "name cannot be empty")
    private String name;

    @NotBlank(message = "address cannot be empty")
    private String address;

    @NotNull
    @Positive(message = "capacity must be positive")
    private Long capacity;
}
