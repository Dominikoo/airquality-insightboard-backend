package pl.dominik.airquality_insightboard.auth.domain;

import java.security.Principal;
import java.util.Set;
import java.util.UUID;

public record AuthenticatedUser(UUID id, String email, Set<String> roles) implements Principal {

    @Override
    public String getName() {
        return id.toString();
    }
}
