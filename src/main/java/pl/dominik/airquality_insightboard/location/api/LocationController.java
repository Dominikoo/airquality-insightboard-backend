package pl.dominik.airquality_insightboard.location.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.dominik.airquality_insightboard.location.application.LocationQueryService;
import pl.dominik.airquality_insightboard.location.domain.Location;
import pl.dominik.airquality_insightboard.airquality.application.AirQualitySnapshotRepository;
import pl.dominik.airquality_insightboard.airquality.domain.AirQualitySnapshot;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationQueryService locationQueryService;
    private final AirQualitySnapshotRepository airQualitySnapshotRepository;

    @GetMapping
    public List<LocationResponse> getAllLocations() {
        List<Location> locations = locationQueryService.findAll();
        List<UUID> ids = locations.stream()
                .map(Location::getId)
                .toList();

        Map<UUID, AirQualitySnapshot> snapshotsByLocationId =
                airQualitySnapshotRepository.findByLocationIds(ids);

        return locations.stream()
                .map(loc -> {
                    AirQualitySnapshot snapshot = snapshotsByLocationId.get(loc.getId());

                    LocationResponse.AirQualitySummary airQualitySummary = null;
                    if (snapshot != null) {
                        airQualitySummary = new LocationResponse.AirQualitySummary(
                                snapshot.getIndex(),
                                snapshot.getPm10(),
                                snapshot.getPm25(),
                                snapshot.getNo2(),
                                snapshot.getSo2(),
                                snapshot.getO3(),
                                snapshot.getUpdatedAt()
                        );
                    }

                    return new LocationResponse(
                            loc.getId(),
                            loc.getName(),
                            loc.getCountryCode(),
                            loc.getLatitude(),
                            loc.getLongitude(),
                            loc.getPopulation(),
                            airQualitySummary
                    );
                })
                .toList();
    }

    public record LocationResponse(
            UUID id,
            String name,
            String countryCode,
            double latitude,
            double longitude,
            long population,
            AirQualitySummary airQuality
    ) {

        public record AirQualitySummary(
                Integer index,
                Double pm10,
                Double pm25,
                Double no2,
                Double so2,
                Double o3,
                Instant updatedAt
        ) {}
    }
}
