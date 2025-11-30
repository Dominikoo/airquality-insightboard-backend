package pl.dominik.airquality_insightboard.airquality.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dominik.airquality_insightboard.airquality.domain.CurrentAirQuality;
import pl.dominik.airquality_insightboard.airquality.domain.AirQualitySnapshot;
import pl.dominik.airquality_insightboard.airquality.domain.Measurement;
import pl.dominik.airquality_insightboard.location.domain.Location;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AirQualityUpdateService {

    private static final double PM25_LIMIT = 25.0;
    private static final double PM10_LIMIT = 50.0;
    private static final double NO2_LIMIT  = 200.0;
    private static final double SO2_LIMIT  = 350.0;
    private static final double O3_LIMIT   = 120.0;

    private static final int MAX_INDEX = 500;
    private static final int MIN_INDEX = 0;
    private static final double PERCENT = 100.0;

    private final AirQualitySnapshotRepository airQualityRepository;

    public AirQualitySnapshot updateSnapshot(Location location, CurrentAirQuality current) {
        Double pm10 = latestValueForParameter(current.measurements(), "pm10");
        Double pm25 = latestValueForParameter(current.measurements(), "pm25");
        Double so2  = latestValueForParameter(current.measurements(), "so2");
        Double no2  = latestValueForParameter(current.measurements(), "no2");
        Double o3   = latestValueForParameter(current.measurements(), "o3");

        Integer index = computeIndex(pm10, pm25, so2, no2, o3);

        AirQualitySnapshot snapshot = new AirQualitySnapshot(
                location.getId(),
                pm10,
                pm25,
                so2,
                no2,
                o3,
                index,
                Instant.now()
        );

        return airQualityRepository.save(snapshot);
    }

    private Double latestValueForParameter(List<Measurement> measurements, String parameterName) {
        return measurements.stream()
                .filter(m -> m.parameter() != null && parameterName.equalsIgnoreCase(m.parameter().name()))
                .max(Comparator.comparing(Measurement::timestamp))
                .map(Measurement::value)
                .orElse(null);
    }

    private Integer computeIndex(Double pm10, Double pm25, Double so2, Double no2, Double o3) {
        double max = 0.0;

        if (pm25 != null) {
            max = Math.max(max, pm25 / PM25_LIMIT * PERCENT);
        }
        if (pm10 != null) {
            max = Math.max(max, pm10 / PM10_LIMIT * PERCENT);
        }
        if (no2 != null) {
            max = Math.max(max, no2 / NO2_LIMIT * PERCENT);
        }
        if (so2 != null) {
            max = Math.max(max, so2 / SO2_LIMIT * PERCENT);
        }
        if (o3 != null) {
            max = Math.max(max, o3 / O3_LIMIT * PERCENT);
        }

        if (max == 0.0) {
            return null;
        }

        int idx = (int) Math.round(max);
        idx = Math.max(MIN_INDEX, Math.min(MAX_INDEX, idx));

        return idx;
    }
}
