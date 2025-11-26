package pl.dominik.airquality_insightboard.airquality.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.dominik.airquality_insightboard.airquality.application.AirQualityService;
import pl.dominik.airquality_insightboard.airquality.domain.CurrentAirQuality;

@RestController
@RequestMapping("/api/air-quality")
@RequiredArgsConstructor
public class AirQualityController {

    private final AirQualityService airQualityService;

    @GetMapping("/current")
    public CurrentAirQualityResponse getCurrent(
            @RequestParam("lat") double latitude,
            @RequestParam("lon") double longitude
    ) {
        CurrentAirQuality current = airQualityService.getCurrent(latitude, longitude);

        return new CurrentAirQualityResponse(
                current.latitude(),
                current.longitude(),
                current.measurements().stream()
                        .map(m -> new CurrentAirQualityResponse.MeasurementDto(
                                m.timestamp(),
                                new CurrentAirQualityResponse.MeasurementDto.ParameterDto(
                                        m.parameter().id(),
                                        m.parameter().name(),
                                        m.parameter().units(),
                                        m.parameter().displayName()
                                ),
                                m.value(),
                                m.sensorId()
                        ))
                        .toList()
        );
    }
}