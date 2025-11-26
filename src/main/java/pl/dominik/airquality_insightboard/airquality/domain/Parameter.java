package pl.dominik.airquality_insightboard.airquality.domain;

public record Parameter(
        long id,
        String name,
        String units,
        String displayName
) {}
