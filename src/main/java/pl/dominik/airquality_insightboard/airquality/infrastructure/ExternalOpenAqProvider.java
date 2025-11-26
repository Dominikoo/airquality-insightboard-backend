package pl.dominik.airquality_insightboard.airquality.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.dominik.airquality_insightboard.airquality.application.AirQualityProvider;
import pl.dominik.airquality_insightboard.airquality.domain.CurrentAirQuality;
import pl.dominik.airquality_insightboard.airquality.domain.Measurement;
import pl.dominik.airquality_insightboard.airquality.domain.Parameter;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ExternalOpenAqProvider implements AirQualityProvider {

    private static final int SEARCH_RADIUS_METERS = 10_000;   // 10 km
    private static final int LOCATIONS_LIMIT = 5;
    private static final String HEADER_API_KEY = "X-API-Key";

    private final WebClient webClient;
    private final Map<Long, Parameter> sensorCache = new ConcurrentHashMap<>();

    public ExternalOpenAqProvider(AirQualityApiProperties properties) {
        Objects.requireNonNull(properties, "AirQualityApiProperties must not be null");

        this.webClient = WebClient.builder()
                .baseUrl(properties.baseUrl())
                .defaultHeader(HEADER_API_KEY, properties.apiKey())
                .filter((request, next) -> {
                    log.debug("OpenAQ request: {} {}", request.method(), request.url());
                    return next.exchange(request);
                })
                .build();
    }

    @Override
    public CurrentAirQuality getCurrentAirQuality(double latitude, double longitude) {
        List<LocationResult> locations = fetchLocationsNear(latitude, longitude);

        for (LocationResult location : locations) {
            OpenAqLatestResponse latest = fetchLatestForLocation(location.id());
            if (latest != null && !latest.results().isEmpty()) {
                List<Measurement> measurements = mapLatestToMeasurements(latest);
                return new CurrentAirQuality(latitude, longitude, measurements);
            }
        }

        throw new RuntimeException("No latest measurements available for coordinates %f,%f"
                .formatted(latitude, longitude));
    }

    // -------------------------------------------------------------------------
    // Private helpers – locations + latest
    // -------------------------------------------------------------------------

    private List<LocationResult> fetchLocationsNear(double latitude, double longitude) {
        OpenAqLocationsResponse locations = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/locations")
                        .queryParam("coordinates", coordinates(latitude, longitude))
                        .queryParam("radius", SEARCH_RADIUS_METERS)
                        .queryParam("limit", LOCATIONS_LIMIT)
                        .build())
                .retrieve()
                .bodyToMono(OpenAqLocationsResponse.class)
                .block();

        if (locations == null || locations.results().isEmpty()) {
            throw new RuntimeException("No locations found near coordinates %f,%f"
                    .formatted(latitude, longitude));
        }

        return locations.results();
    }

    private OpenAqLatestResponse fetchLatestForLocation(Integer locationId) {
        OpenAqLatestResponse latest = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/locations/{id}/latest")
                        .build(locationId))
                .retrieve()
                .bodyToMono(OpenAqLatestResponse.class)
                .block();

        if (latest == null || latest.results().isEmpty()) {
            log.debug("No latest measurements for location {}", locationId);
        }

        return latest;
    }

    private List<Measurement> mapLatestToMeasurements(OpenAqLatestResponse latest) {
        return latest.results().stream()
                .map(m -> {
                    Parameter parameter = fetchParameterForSensor(m.sensorsId());
                    return new Measurement(
                            Instant.parse(m.datetime().utc()),
                            parameter,
                            m.value(),
                            m.sensorsId()
                    );
                })
                .toList();
    }

    private String coordinates(double latitude, double longitude) {
        return latitude + "," + longitude;
    }

    private Parameter fetchParameterForSensor(long sensorId) {
        Parameter cached = sensorCache.get(sensorId);
        if (cached != null) {
            return cached;
        }

        OpenAqSensorResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/sensors/{id}")
                        .build(sensorId))
                .retrieve()
                .bodyToMono(OpenAqSensorResponse.class)
                .block();

        if (response == null || response.results().isEmpty()) {
            throw new RuntimeException("No sensor details found for sensorId " + sensorId);
        }

        SensorResult sensor = response.results().get(0);
        SensorParameter p = sensor.parameter();

        Parameter parameter = new Parameter(
                p.id(),
                p.name(),
                p.units(),
                p.displayName()
        );

        sensorCache.put(sensorId, parameter);
        return parameter;
    }

    // -------------------------------------------------------------------------
    // JSON mapping records – locations + latest
    // -------------------------------------------------------------------------

    private record OpenAqLocationsResponse(
            List<LocationResult> results
    ) {}

    private record LocationResult(
            Integer id,
            String name
    ) {}

    private record OpenAqLatestResponse(
            List<LatestResult> results
    ) {}

    private record LatestResult(
            LatestDateTime datetime,
            Double value,
            long sensorsId,
            Integer parameterId
    ) {}

    private record LatestDateTime(
            String utc,
            String local
    ) {}

    // -------------------------------------------------------------------------
    // JSON mapping records – sensors
    // -------------------------------------------------------------------------

    private record OpenAqSensorResponse(
            List<SensorResult> results
    ) {}

    private record SensorResult(
            long id,
            String name,
            SensorParameter parameter
    ) {}

    private record SensorParameter(
            long id,
            String name,
            String units,
            String displayName
    ) {}
}