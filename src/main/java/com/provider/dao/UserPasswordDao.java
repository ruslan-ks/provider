package com.provider.dao;

import com.provider.dao.exception.DBException;
import com.provider.entity.user.UserPassword;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class UserPasswordDao extends EntityDao<Long, UserPassword> {
    /**
     * Find user password by user id
     * @param userId user id
     * @return optional object containing found record, Optional.empty() if no record found
     * @throws DBException if SQLException occurred
     */
    public abstract @NotNull Optional<UserPassword> findByKey(@NotNull Long userId) throws DBException;
}
