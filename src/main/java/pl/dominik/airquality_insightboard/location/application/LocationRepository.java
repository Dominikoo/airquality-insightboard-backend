package pl.dominik.airquality_insightboard.location.application;

import pl.dominik.airquality_insightboard.location.domain.Location;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LocationRepository {

    Optional<Location> findById(UUID id);

    Optional<Location> findByExternalId(Long externalId);

    List<Location> findAll();

    Location save(Location location);

    List<Location> saveAll(List<Location> locations);
}