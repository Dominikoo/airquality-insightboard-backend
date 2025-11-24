package pl.dominik.airquality_insightboard.user.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dominik.airquality_insightboard.user.infrastructure.entity.RoleJpaEntity;

import java.util.Optional;
import java.util.UUID;

public interface RoleDataJpaRepository extends JpaRepository<RoleJpaEntity, UUID> {
    Optional<RoleJpaEntity> findByName(String name);
}
