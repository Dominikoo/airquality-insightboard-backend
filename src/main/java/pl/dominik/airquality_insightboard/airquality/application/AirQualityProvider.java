package pl.dominik.airquality_insightboard.airquality.application;

import pl.dominik.airquality_insightboard.airquality.domain.CurrentAirQuality;

public interface AirQualityProvider {

    CurrentAirQuality getCurrentAirQuality(double latitude, double longitude);
}
