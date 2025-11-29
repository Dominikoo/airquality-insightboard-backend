package pl.dominik.airquality_insightboard.location.infrastructure.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class CitiesJsonClient {

    private static final String URL =
            "https://raw.githubusercontent.com/lmfmaier/cities-json/master/cities500.json";

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public CitiesJsonClient(ObjectMapper objectMapper) {
        this.webClient = WebClient.builder()
                .codecs(configurer ->
                        configurer.defaultCodecs().maxInMemorySize(100 * 1024 * 1024)
                )
                .build();
        this.objectMapper = objectMapper;
    }

    public List<ExternalCity> fetchAllCities() {
        log.info("Fetching cities dataset from {}", URL);
        try {
            String json = webClient.get()
                    .uri(URL)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> response.createException().map(RuntimeException::new)
                    )
                    .bodyToMono(String.class)
                    .block();
            ExternalCity[] cities = objectMapper.readValue(json, ExternalCity[].class);
            return List.of(cities);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot fetch or parse cities dataset from " + URL, e);
        }
    }

    public record ExternalCity(
            String id,
            String name,
            String country,
            String admin1,
            String admin2,
            String lat,
            String lon,
            String pop
    ) {}
}
