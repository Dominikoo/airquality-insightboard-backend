package pl.dominik.airquality_insightboard.user.api;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String email
) {
}
