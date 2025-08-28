package my.ddos.model.dto.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import my.ddos.model.dto.booking.BookingResponse;
import my.ddos.model.dto.role.RoleResponse;
import my.ddos.model.entity.Booking;
import my.ddos.model.entity.Role;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;

    private String username;

    private String fullName;

    private LocalDateTime createdAt;

    private Set<RoleResponse> userRoles = new HashSet<>();

    private List<BookingResponse> bookings = new ArrayList<>();
}
