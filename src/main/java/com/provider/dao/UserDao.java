package com.provider.dao;

import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class UserDao extends EntityDao<Long, User> {
    public abstract Optional<User> findByLogin(@NotNull String login) throws DBException;
}
