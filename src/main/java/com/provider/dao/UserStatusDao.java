package com.provider.dao;

import com.provider.dao.exception.DBException;
import com.provider.entity.user.UserStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class UserStatusDao extends EntityDao<Integer, UserStatus> {
    /**
     * Returns current UserStatus of a user
     * @param userId user id
     * @return Optional containing UserStatus containing current status information if found,
     * empty optional otherwise
     */
    public abstract @NotNull Optional<UserStatus> findCurrentUserStatus(long userId) throws DBException;
}
