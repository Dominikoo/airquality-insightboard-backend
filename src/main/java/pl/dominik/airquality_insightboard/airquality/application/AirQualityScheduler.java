package pl.dominik.airquality_insightboard.airquality.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.dominik.airquality_insightboard.location.application.LocationRepository;
import pl.dominik.airquality_insightboard.location.domain.Location;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class AirQualityScheduler {

    private final LocationRepository locationRepository;
    private final AirQualityProvider airQualityProvider;
    private final AirQualityUpdateService updateService;

    @Scheduled(fixedDelayString = "PT10M")
    public void refreshAirQualityForTopCities() {
        List<Location> locations = locationRepository.findAll();

        for (Location location : locations) {
            try {
                var current = airQualityProvider.getCurrentAirQuality(
                        location.getLatitude(),
                        location.getLongitude()
                );
                updateService.updateSnapshot(location, current);
            } catch (Exception ex) {
                log.warn("Failed to update air quality for location {}: {}",
                        location.getId(), ex.getMessage());
            }
        }
    }
}
