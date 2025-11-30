package pl.dominik.airquality_insightboard.airquality.infrastructure.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "airquality.api")
public record AirQualityApiProperties(
        String baseUrl,
        String apiKey
) { }
