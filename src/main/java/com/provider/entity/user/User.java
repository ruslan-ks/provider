package com.provider.entity.user;

import com.provider.entity.Entity;
import org.jetbrains.annotations.NotNull;

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

    /**
     * Sets user id
     * @param id new id
     * @throws IllegalArgumentException if id <= 0
     */
    void setId(long id);

    @NotNull String getName();

    @NotNull String getSurname();

    @NotNull String getLogin();

    @NotNull String getPhone();

    @NotNull Role getRole();

    @NotNull Status getStatus();
}
