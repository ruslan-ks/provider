package com.provider.entity.user;

import com.provider.entity.Entity;
import org.jetbrains.annotations.NotNull;

/**
 * User entity
 */
public interface User extends Entity {
    enum Role {
        ROOT,
        ADMIN,
        MEMBER,
        GUEST
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
