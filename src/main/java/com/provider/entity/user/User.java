package com.provider.entity.user;

import com.provider.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface User extends Entity {
    enum Role {
        root,
        admin,
        member,
        guest
    }

    long getId();

    /**
     * Sets user id
     * @param id new id
     * @throws IllegalArgumentException if id <= 0
     */
    void setId(long id);

    String getName();

    String getSurname();

    String getLogin();

    String getPhone();

    Role getRole();
}
