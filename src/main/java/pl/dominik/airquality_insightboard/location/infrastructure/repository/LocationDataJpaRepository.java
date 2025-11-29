package pl.dominik.airquality_insightboard.location.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dominik.airquality_insightboard.location.infrastructure.entity.LocationJpaEntity;

import java.util.Optional;
import java.util.UUID;

interface LocationDataJpaRepository extends JpaRepository<LocationJpaEntity, UUID> {

    Optional<LocationJpaEntity> findByExternalId(Long externalId);
}