package pl.dominik.airquality_insightboard.user.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.dominik.airquality_insightboard.user.application.RoleRepository;
import pl.dominik.airquality_insightboard.user.domain.Role;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleDataJpaRepository jpaRepository;

    @Override
    public Optional<Role> findByName(String name) {
        return jpaRepository.findByName(name)
                .map(jpa -> new Role(jpa.getId(), jpa.getName()));
    }
}
