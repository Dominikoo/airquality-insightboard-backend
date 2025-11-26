package pl.dominik.airquality_insightboard.airquality.domain;

import java.util.List;

public record CurrentAirQuality(
        double latitude,
        double longitude,
        List<Measurement> measurements
) { }
