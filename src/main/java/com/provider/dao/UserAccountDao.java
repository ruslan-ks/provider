package com.provider.dao;

import com.provider.dao.exception.DBException;
import com.provider.entity.user.UserAccount;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public abstract class UserAccountDao extends EntityDao<Long, UserAccount> {
    /**
     * Finds user account by id
     * @param id user account id
     * @return Optional containing UserAccount if found, empty optional otherwise
     */
    @Override
    public abstract @NotNull Optional<UserAccount> findByKey(@NotNull Long id) throws DBException;

    /**
     * Returns all accounts belonging to the user with a specified id
     * @param userId account owner id
     * @return List of UserAccount objects if found, empty list otherwise
     */
    public abstract @NotNull List<UserAccount> findAll(long userId) throws DBException;
}
