package pl.dominik.airquality_insightboard.airquality.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.dominik.airquality_insightboard.airquality.domain.AirQualitySnapshot;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "air_quality")
@Getter
@NoArgsConstructor
public class AirQualityJpaEntity {

    @Id
    @Column(name = "location_id", nullable = false, updatable = false)
    private UUID locationId;

    @Column(name = "pm10")
    private Double pm10;

    @Column(name = "pm25")
    private Double pm25;

    @Column(name = "so2")
    private Double so2;

    @Column(name = "no2")
    private Double no2;

    @Column(name = "o3")
    private Double o3;

    @Column(name = "aq_index")
    private Integer index;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    private AirQualityJpaEntity(UUID locationId,
                                Double pm10,
                                Double pm25,
                                Double so2,
                                Double no2,
                                Double o3,
                                Integer index,
                                Instant updatedAt) {
        this.locationId = locationId;
        this.pm10 = pm10;
        this.pm25 = pm25;
        this.so2 = so2;
        this.no2 = no2;
        this.o3 = o3;
        this.index = index;
        this.updatedAt = updatedAt;
    }

    public static AirQualityJpaEntity fromDomain(AirQualitySnapshot snapshot) {
        return new AirQualityJpaEntity(
                snapshot.getLocationId(),
                snapshot.getPm10(),
                snapshot.getPm25(),
                snapshot.getSo2(),
                snapshot.getNo2(),
                snapshot.getO3(),
                snapshot.getIndex(),
                snapshot.getUpdatedAt()
        );
    }

    public AirQualitySnapshot toDomain() {
        return new AirQualitySnapshot(
                this.locationId,
                this.pm10,
                this.pm25,
                this.so2,
                this.no2,
                this.o3,
                this.index,
                this.updatedAt
        );
    }

    public void updateFromDomain(AirQualitySnapshot snapshot) {
        this.pm10 = snapshot.getPm10();
        this.pm25 = snapshot.getPm25();
        this.so2 = snapshot.getSo2();
        this.no2 = snapshot.getNo2();
        this.o3 = snapshot.getO3();
        this.index = snapshot.getIndex();
        this.updatedAt = snapshot.getUpdatedAt();
    }
}
