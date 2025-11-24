package pl.dominik.airquality_insightboard.user.application;

import pl.dominik.airquality_insightboard.user.domain.Role;

import java.util.Optional;

public interface RoleRepository {

    Optional<Role> findByName(String name);
}
