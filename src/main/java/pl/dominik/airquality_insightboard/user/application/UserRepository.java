package pl.dominik.airquality_insightboard.user.application;

import pl.dominik.airquality_insightboard.user.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID id);

    User save(User user);

    boolean existsByEmail(String email);
}
