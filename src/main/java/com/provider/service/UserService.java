package com.provider.service;

import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import com.provider.entity.user.UserPassword;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Contains business logic and db access for User class data
 */
public interface UserService {
    /**
     * Find user by id
     * @param id user id
     * @return Optional containing user with a specified id if found, empty optional otherwise
     */
    @NotNull Optional<User> findUserById(long id) throws DBException;

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
    @NotNull Optional<User> authenticate(@NotNull String login, @NotNull String password) throws DBException;
}
