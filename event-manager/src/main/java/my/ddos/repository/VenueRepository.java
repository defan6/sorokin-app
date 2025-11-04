package my.ddos.repository;

import jakarta.validation.constraints.NotBlank;
import my.ddos.model.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
    boolean existsByName(@NotBlank String name);
}
