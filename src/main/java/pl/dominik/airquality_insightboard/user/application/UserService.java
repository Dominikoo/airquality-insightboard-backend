package pl.dominik.airquality_insightboard.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.dominik.airquality_insightboard.user.api.CreateUserRequest;
import pl.dominik.airquality_insightboard.user.domain.Role;
import pl.dominik.airquality_insightboard.user.domain.User;
import pl.dominik.airquality_insightboard.user.application.exception.EmailAlreadyExistsException;
import pl.dominik.airquality_insightboard.user.application.exception.RoleNotFoundException;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(CreateUserRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RoleNotFoundException("ROLE_USER"));

        String passwordHash = passwordEncoder.encode(request.password());

        User user = User.newUser(
                request.email(),
                passwordHash,
                Set.of(userRole)
        );

        return userRepository.save(user);
    }
}