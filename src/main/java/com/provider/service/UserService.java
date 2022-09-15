package com.provider.service;

import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import com.provider.entity.user.UserPassword;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface UserService {
    /**
     * Searches for user in db
     * @param login user login
     * @return Optional containing User with specified login if found, Optional.empty() otherwise
     */
    @NotNull Optional<User> findUserByLogin(@NotNull String login) throws DBException;

    /**
     * Searches for user password in db
     * @param userId user id
     * @return Optional containing UserPassword with specified if found, Optional.empty() otherwise
     */
    @NotNull Optional<UserPassword> findUserPassword(long userId) throws DBException;

    /**
     * Inserts user to db
     * @param user new user to be saved
     * @param userPassword new user password
     * @return true if db changes were mage
     */
    boolean insertUser(@NotNull User user, @NotNull UserPassword userPassword) throws DBException;

    /**
     * Checks login and password and returns authenticated user object
     * @param login user-provided login
     * @param password user-provided password(NOT a hash)
     * @return Optional containing user object if login and password are correct, Optional.empty() otherwise
     */
    @NotNull Optional<User> authenticate(String login, String password) throws DBException;
}
