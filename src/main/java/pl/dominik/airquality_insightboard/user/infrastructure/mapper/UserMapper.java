package pl.dominik.airquality_insightboard.user.infrastructure.mapper;

import org.springframework.stereotype.Component;
import pl.dominik.airquality_insightboard.user.domain.Role;
import pl.dominik.airquality_insightboard.user.domain.User;
import pl.dominik.airquality_insightboard.user.infrastructure.entity.UserJpaEntity;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User toDomain(UserJpaEntity entity) {
        return new User(
                entity.getId(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.isEnabled(),
                entity.getRoles().stream()
                        .map(r -> new Role(r.getId(), r.getName()))
                        .collect(Collectors.toSet()),
                entity.getCreatedAt(),
                entity.getLastLoginAt()
        );
    }

    public UserJpaEntity toEntity(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.getId());
        entity.setEmail(user.getEmail());
        entity.setPasswordHash(user.getPasswordHash());
        entity.setEnabled(user.isEnabled());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setLastLoginAt(user.getLastLoginAt());
        return entity;
    }
}
