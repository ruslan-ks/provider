package com.provider.entity.user;

import com.provider.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

/**
 * User entity
 */
public interface User extends Entity {
    enum Role {
        ROOT,
        ADMIN,
        MEMBER,
        GUEST;

        public static @NotNull Set<Role> rolesAllowedForCreation(@NotNull Role role) {
            final Map<Role, Set<Role>> rolesAllowedForCreationMap = Map.of(
                    Role.ROOT, Set.of(User.Role.ADMIN, User.Role.MEMBER),
                    Role.ADMIN, Set.of(User.Role.MEMBER)
            );
            return rolesAllowedForCreationMap.get(role);
        }
    }

    enum Status {
        ACTIVE,
        SUSPENDED
    }

    long getId();

    void setId(long id);

    @NotNull String getName();

    @NotNull String getSurname();

    @NotNull String getLogin();

    @NotNull String getPhone();

    @NotNull Role getRole();

    @NotNull Status getStatus();

    void setStatus(@NotNull Status status);
}
