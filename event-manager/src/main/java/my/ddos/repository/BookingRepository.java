package my.ddos.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import my.ddos.model.dto.booking.BookingResponse;
import my.ddos.model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByUserUsername(String username);

    boolean existsByUserUsernameAndEventId(String username, @NotBlank @Positive Long aLong);
}
