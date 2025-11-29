package pl.dominik.airquality_insightboard.location.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.dominik.airquality_insightboard.location.application.LocationRepository;
import pl.dominik.airquality_insightboard.location.domain.Location;
import pl.dominik.airquality_insightboard.location.infrastructure.entity.LocationJpaEntity;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LocationRepositoryImpl implements LocationRepository {

    private final LocationDataJpaRepository jpaRepository;

    @Override
    public Optional<Location> findById(java.util.UUID id) {
        return jpaRepository.findById(id).map(LocationJpaEntity::toDomain);
    }

    @Override
    public Optional<Location> findByExternalId(Long externalId) {
        return jpaRepository.findByExternalId(externalId).map(LocationJpaEntity::toDomain);
    }

    @Override
    public List<Location> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(LocationJpaEntity::toDomain)
                .toList();
    }

    @Override
    public Location save(Location location) {
        LocationJpaEntity entity = LocationJpaEntity.fromDomain(location);
        LocationJpaEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public List<Location> saveAll(List<Location> locations) {
        List<LocationJpaEntity> entities = locations.stream()
                .map(LocationJpaEntity::fromDomain)
                .toList();

        return jpaRepository.saveAll(entities).stream()
                .map(LocationJpaEntity::toDomain)
                .toList();
    }
}
