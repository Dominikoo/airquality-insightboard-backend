package pl.dominik.airquality_insightboard.user.domain;

import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public class Role {

    private final UUID id;
    private final String name;

    public Role(UUID id, String name) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
    }

    public static Role ofName(String name) {
        return new Role(UUID.randomUUID(), name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role role)) return false;
        return name.equals(role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
