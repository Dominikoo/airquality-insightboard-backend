package pl.dominik.airquality_insightboard.location.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dominik.airquality_insightboard.location.domain.Location;
import pl.dominik.airquality_insightboard.location.infrastructure.client.CitiesJsonClient;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LocationImportService {

    private final LocationRepository locationRepository;
    private final CitiesJsonClient citiesJsonClient;

    @Transactional
    public int importTopCities(int limit, String country) {
        if (limit <= 0) {
            throw new IllegalArgumentException("limit must be positive");
        }

        List<CitiesJsonClient.ExternalCity> allCities = citiesJsonClient.fetchAllCities();

        List<Location> topLocations = allCities.stream()
                .filter(c -> c.pop() != null && !c.pop().isBlank())
                .filter(c -> country == null || Objects.equals(c.country(), country))
                .sorted((c1, c2) -> Integer.parseInt(c2.pop()) - Integer.parseInt(c1.pop()))
                .limit(limit)
                .map(this::mapToLocationDomain)
                .toList();

        List<Location> toSave = topLocations.stream()
                .map(loc -> locationRepository.findByExternalId(loc.getExternalId())
                        .map(existing -> existing.withUpdatedData(
                                loc.getName(),
                                loc.getCountryCode(),
                                loc.getLatitude(),
                                loc.getLongitude(),
                                loc.getPopulation()
                        ))
                        .orElse(loc)
                )
                .toList();

        locationRepository.saveAll(toSave);
        return toSave.size();
    }

    private Location mapToLocationDomain(CitiesJsonClient.ExternalCity c) {
        long externalId = Long.parseLong(c.id());
        long population = Long.parseLong(c.pop());

        double lat = Double.parseDouble(c.lat());
        double lon = Double.parseDouble(c.lon());

        return Location.createNew(
                c.name(),
                c.country(),
                lat,
                lon,
                population,
                externalId
        );
    }
}