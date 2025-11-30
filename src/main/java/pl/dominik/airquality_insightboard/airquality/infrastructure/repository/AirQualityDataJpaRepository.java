package pl.dominik.airquality_insightboard.airquality.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dominik.airquality_insightboard.airquality.infrastructure.entity.AirQualityJpaEntity;

import java.util.List;
import java.util.UUID;

interface AirQualityDataJpaRepository extends JpaRepository<AirQualityJpaEntity, UUID> {

    List<AirQualityJpaEntity> findByLocationIdIn(List<UUID> locationIds);
}
