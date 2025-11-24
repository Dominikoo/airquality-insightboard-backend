package pl.dominik.airquality_insightboard.auth.domain;

import java.util.Set;
import java.util.UUID;

public record JwtUserPayload(
        UUID userId,
        String email,
        Set<String> roles
) { }
