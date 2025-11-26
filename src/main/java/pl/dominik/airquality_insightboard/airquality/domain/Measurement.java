package pl.dominik.airquality_insightboard.airquality.domain;

import java.time.Instant;

public record Measurement(
        Instant timestamp,
        Parameter parameter,
        double value,
        long sensorId
) { }
