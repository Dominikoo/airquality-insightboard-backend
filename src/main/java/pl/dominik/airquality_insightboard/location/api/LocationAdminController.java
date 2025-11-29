package pl.dominik.airquality_insightboard.location.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.dominik.airquality_insightboard.location.application.LocationImportService;

@RestController
@RequestMapping("/api/admin/locations")
@RequiredArgsConstructor
public class LocationAdminController {

    private final LocationImportService importService;

    @PostMapping("/import-top-cities")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ImportResultResponse importTopCities(
            @RequestParam(defaultValue = "10000") int limit,
            @RequestParam(required = false) String country
    ) {
        int imported = importService.importTopCities(limit, country);
        return new ImportResultResponse(imported);
    }

    public record ImportResultResponse(
            int importedCount
    ) {}
}
