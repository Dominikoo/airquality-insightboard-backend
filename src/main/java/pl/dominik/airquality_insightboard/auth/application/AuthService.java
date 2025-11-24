package pl.dominik.airquality_insightboard.auth.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.dominik.airquality_insightboard.auth.api.LoginRequest;
import pl.dominik.airquality_insightboard.auth.api.LoginResponse;
import pl.dominik.airquality_insightboard.auth.application.exception.InvalidCredentialsException;
import pl.dominik.airquality_insightboard.auth.infrastrucutre.JwtTokenProvider;
import pl.dominik.airquality_insightboard.user.application.UserRepository;
import pl.dominik.airquality_insightboard.user.domain.User;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new InvalidCredentialsException());

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtTokenProvider.generateToken(user);
        return new LoginResponse(token);
    }
}
