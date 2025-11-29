package pl.dominik.airquality_insightboard.location.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.dominik.airquality_insightboard.location.domain.Location;

import java.util.UUID;

@Entity
@Table(name = "locations")
@Getter
@NoArgsConstructor
public class LocationJpaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "country_code", nullable = false, length = 2)
    private String countryCode;

    @Column(name = "latitude", nullable = false)
    private double latitude;

    @Column(name = "longitude", nullable = false)
    private double longitude;

    @Column(name = "population", nullable = false)
    private long population;

    @Column(name = "external_id", nullable = false, unique = true)
    private long externalId;

    private LocationJpaEntity(UUID id,
                              String name,
                              String countryCode,
                              double latitude,
                              double longitude,
                              long population,
                              long externalId) {
        this.id = id;
        this.name = name;
        this.countryCode = countryCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.population = population;
        this.externalId = externalId;
    }

    public static LocationJpaEntity fromDomain(Location location) {
        return new LocationJpaEntity(
                location.getId(),
                location.getName(),
                location.getCountryCode(),
                location.getLatitude(),
                location.getLongitude(),
                location.getPopulation(),
                location.getExternalId()
        );
    }

    public Location toDomain() {
        return new Location(
                this.id,
                this.name,
                this.countryCode,
                this.latitude,
                this.longitude,
                this.population,
                this.externalId
        );
    }

    public void updateFromDomain(Location location) {
        this.name = location.getName();
        this.countryCode = location.getCountryCode();
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.population = location.getPopulation();
    }
}
