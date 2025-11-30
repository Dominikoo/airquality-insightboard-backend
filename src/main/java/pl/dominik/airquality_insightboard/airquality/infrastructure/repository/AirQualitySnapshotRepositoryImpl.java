package pl.dominik.airquality_insightboard.airquality.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.dominik.airquality_insightboard.airquality.application.AirQualitySnapshotRepository;
import pl.dominik.airquality_insightboard.airquality.domain.AirQualitySnapshot;
import pl.dominik.airquality_insightboard.airquality.infrastructure.entity.AirQualityJpaEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AirQualitySnapshotRepositoryImpl implements AirQualitySnapshotRepository {

    private final AirQualityDataJpaRepository jpaRepository;

    @Override
    public Optional<AirQualitySnapshot> findByLocationId(UUID locationId) {
        return jpaRepository.findById(locationId).map(AirQualityJpaEntity::toDomain);
    }

    @Override
    public Map<UUID, AirQualitySnapshot> findByLocationIds(List<UUID> locationIds) {
        return jpaRepository.findByLocationIdIn(locationIds).stream()
                .map(AirQualityJpaEntity::toDomain)
                .collect(Collectors.toMap(
                        AirQualitySnapshot::getLocationId,
                        snapshot -> snapshot
                ));
    }

    @Override
    public AirQualitySnapshot save(AirQualitySnapshot snapshot) {
        AirQualityJpaEntity entity = jpaRepository
                .findById(snapshot.getLocationId())
                .orElseGet(() -> AirQualityJpaEntity.fromDomain(snapshot));

        entity.updateFromDomain(snapshot);
        AirQualityJpaEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }
}
