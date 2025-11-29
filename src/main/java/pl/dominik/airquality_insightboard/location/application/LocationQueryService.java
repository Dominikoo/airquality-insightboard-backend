package pl.dominik.airquality_insightboard.location.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dominik.airquality_insightboard.location.domain.Location;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationQueryService {

    private final LocationRepository locationRepository;

    public List<Location> findAll() {
        return locationRepository.findAll();
    }
}