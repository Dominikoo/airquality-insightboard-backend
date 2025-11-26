package pl.dominik.airquality_insightboard.airquality.api;

import java.time.Instant;
import java.util.List;

public record CurrentAirQualityResponse(
        double latitude,
        double longitude,
        List<MeasurementDto> measurements
) {
    public record MeasurementDto(
            Instant timestamp,
            ParameterDto parameter,
            double value,
            long sensorId
    ) {
        public record ParameterDto(
                long id,
                String name,
                String units,
                String displayName
        ) {}
    }
}