package pl.dominik.airquality_insightboard.airquality.domain;

import lombok.Getter;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public class AirQualitySnapshot {

    private final UUID locationId;
    private final Double pm10;
    private final Double pm25;
    private final Double so2;
    private final Double no2;
    private final Double o3;
    private final Integer index;
    private final Instant updatedAt;

    public AirQualitySnapshot(UUID locationId,
                              Double pm10,
                              Double pm25,
                              Double so2,
                              Double no2,
                              Double o3,
                              Integer index,
                              Instant updatedAt) {
        this.locationId = Objects.requireNonNull(locationId, "locationId must not be null");
        this.pm10 = pm10;
        this.pm25 = pm25;
        this.so2 = so2;
        this.no2 = no2;
        this.o3 = o3;
        this.index = index;
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt must not be null");
    }
}
