package pl.dominik.airquality_insightboard.airquality.application;

import pl.dominik.airquality_insightboard.airquality.domain.AirQualitySnapshot;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface AirQualitySnapshotRepository {

    Optional<AirQualitySnapshot> findByLocationId(UUID locationId);

    Map<UUID, AirQualitySnapshot> findByLocationIds(List<UUID> locationIds);

    AirQualitySnapshot save(AirQualitySnapshot snapshot);
}
