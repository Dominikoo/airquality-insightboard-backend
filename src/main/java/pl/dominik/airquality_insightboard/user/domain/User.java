package pl.dominik.airquality_insightboard.user.domain;

import lombok.Getter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
public class User {

    private final UUID id;
    private String email;
    private String passwordHash;
    private boolean enabled;
    private final Set<Role> roles = new HashSet<>();
    private final Instant createdAt;
    private Instant lastLoginAt;

    public User(UUID id,
                String email,
                String passwordHash,
                boolean enabled,
                Set<Role> roles,
                Instant createdAt,
                Instant lastLoginAt) {

        this.id = Objects.requireNonNull(id, "id must not be null");
        this.email = Objects.requireNonNull(email, "email must not be null");
        this.passwordHash = Objects.requireNonNull(passwordHash, "passwordHash must not be null");
        this.enabled = enabled;
        this.createdAt = Objects.requireNonNullElseGet(createdAt, Instant::now);
        this.lastLoginAt = lastLoginAt;

        if (roles != null) {
            this.roles.addAll(roles);
        }
    }

    public static User newUser(String email, String passwordHash, Set<Role> initialRoles) {
        return new User(
                UUID.randomUUID(),
                email,
                passwordHash,
                true,
                initialRoles,
                Instant.now(),
                null
        );
    }

    public void changeEmail(String newEmail) {
        this.email = Objects.requireNonNull(newEmail, "newEmail must not be null");
    }

    public void changePassword(String newPasswordHash) {
        this.passwordHash = Objects.requireNonNull(newPasswordHash, "newPasswordHash must not be null");
    }

    public void disable() {
        this.enabled = false;
    }

    public void enable() {
        this.enabled = true;
    }

    public void addRole(Role role) {
        this.roles.add(Objects.requireNonNull(role));
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    public boolean hasRole(String roleName) {
        return roles.stream().anyMatch(r -> r.getName().equals(roleName));
    }

    public void markLoggedInNow() {
        this.lastLoginAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
