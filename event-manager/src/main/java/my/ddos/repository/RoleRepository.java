package my.ddos.repository;

import my.ddos.enums.UserRole;
import my.ddos.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(UserRole userRole);
}
