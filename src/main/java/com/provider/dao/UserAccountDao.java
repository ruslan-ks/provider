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
     * @throws IllegalArgumentException if id == 0
     */
    @Override
    public abstract @NotNull Optional<UserAccount> findByKey(@NotNull Long id) throws DBException;

    /**
     * Returns all accounts belonging to the user with a specified id
     * @param userId account owner id
     * @return List of UserAccount objects if found, empty list otherwise
     * @throws IllegalArgumentException if userId == 0
     */
    public abstract @NotNull List<UserAccount> findAll(long userId) throws DBException;

    /**
     * Updates account data in db
     * @param account user account
     * @return true if db was modified
     * @throws DBException if SQLException occurred
     * @throws IllegalArgumentException if account.getId() returns 0
     */
    public abstract boolean update(@NotNull UserAccount account) throws DBException;
}
