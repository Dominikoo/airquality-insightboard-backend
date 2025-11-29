package pl.dominik.airquality_insightboard.location.domain;

import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public class Location {

    private final UUID id;
    private final String name;
    private final String countryCode; // ISO 3166-1 alpha-2, ie. "UK"
    private final double latitude;
    private final double longitude;
    private final long population;
    private final long externalId;

    public Location(UUID id,
                    String name,
                    String countryCode,
                    double latitude,
                    double longitude,
                    long population,
                    long externalId) {

        this.id = Objects.requireNonNull(id, "id must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.countryCode = Objects.requireNonNull(countryCode, "countryCode must not be null");
        this.latitude = latitude;
        this.longitude = longitude;
        this.population = population;
        this.externalId = externalId;
    }

    public static Location createNew(String name,
                                     String countryCode,
                                     double latitude,
                                     double longitude,
                                     long population,
                                     long externalId) {
        return new Location(
                UUID.randomUUID(),
                name,
                countryCode.toUpperCase(),
                latitude,
                longitude,
                population,
                externalId
        );
    }

    public Location withUpdatedData(String name,
                                    String countryCode,
                                    double latitude,
                                    double longitude,
                                    long population) {
        return new Location(
                this.id,
                name,
                countryCode.toUpperCase(),
                latitude,
                longitude,
                population,
                this.externalId
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}