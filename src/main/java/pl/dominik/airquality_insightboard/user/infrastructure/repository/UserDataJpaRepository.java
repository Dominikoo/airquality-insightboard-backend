package pl.dominik.airquality_insightboard.user.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dominik.airquality_insightboard.user.infrastructure.entity.UserJpaEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserDataJpaRepository extends JpaRepository<UserJpaEntity, UUID> {
    Optional<UserJpaEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
