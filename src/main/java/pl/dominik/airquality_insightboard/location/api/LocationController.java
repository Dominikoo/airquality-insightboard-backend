package pl.dominik.airquality_insightboard.location.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.dominik.airquality_insightboard.location.application.LocationQueryService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationQueryService queryService;

    @GetMapping
    public List<LocationResponse> getAllLocations() {
        return queryService.findAll().stream()
                .map(loc -> new LocationResponse(
                        loc.getId(),
                        loc.getName(),
                        loc.getCountryCode(),
                        loc.getLatitude(),
                        loc.getLongitude(),
                        loc.getPopulation()
                ))
                .toList();
    }

    public record LocationResponse(
            UUID id,
            String name,
            String countryCode,
            double latitude,
            double longitude,
            long population
    ) {}
}
