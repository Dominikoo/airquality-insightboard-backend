package pl.dominik.airquality_insightboard.airquality.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dominik.airquality_insightboard.airquality.domain.CurrentAirQuality;

@Service
@RequiredArgsConstructor
public class AirQualityService {

    private final AirQualityProvider airQualityProvider;

    public CurrentAirQuality getCurrent(double latitude, double longitude) {
        validateCoordinates(latitude, longitude);
        return airQualityProvider.getCurrentAirQuality(latitude, longitude);
    }

    private void validateCoordinates(double latitude, double longitude) {
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }
    }
}