package com.provider.service;

import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import com.provider.entity.user.UserPassword;
import com.provider.service.exception.ValidationException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Contains business logic and db access for User class data
 */
public abstract class UserService extends AbstractService {
    protected UserService() throws DBException {}

    /**
     * Find user by id
     * @param id user id
     * @return Optional containing user with a specified id if found, empty optional otherwise
     */
    public abstract @NotNull Optional<User> findUserById(long id) throws DBException;

    /**
     * Searches for user in db
     * @param login user login
     * @return Optional containing User with specified login if found, Optional.empty() otherwise
     */
    public abstract @NotNull Optional<User> findUserByLogin(@NotNull String login) throws DBException;

    /**
     * Searches for user password in db
     * @param userId user id
     * @return Optional containing UserPassword with specified if found, Optional.empty() otherwise
     */
    public abstract @NotNull Optional<UserPassword> findUserPassword(long userId) throws DBException;

    /**
     * Inserts user to db.
     * @param user new user to be saved
     * @param password new user password
     * @return true if db changes were mage
     * @throws ValidationException if at least one of user properties or password is not valid according to
     * UserValidator calls result.
     */
    public abstract boolean insertUser(@NotNull User user, @NotNull String password)
            throws DBException, ValidationException;

    /**
     * Checks login and password and returns authenticated user object
     * @param login user-provided login
     * @param password user-provided password(NOT a hash)
     * @return Optional containing user object if login and password are correct, Optional.empty() otherwise
     */
    public abstract @NotNull Optional<User> authenticate(@NotNull String login, @NotNull String password)
            throws DBException;

    /**
     * Checks if user is active - not suspended(banned and so on)
     * Does not check user data in a db
     * @param user user to be checked
     * @return true if user is active
     */
    public abstract boolean isActiveUser(@NotNull User user);

    /**
     * Checks whether user is admin or higher
     * @param user user to be checked
     * @return true is user has admin rights
     */
    public abstract boolean hasAdminRights(@NotNull User user);

    /**
     * Checks whether user has root rights
     * @param user user to be checked
     * @return true is user has root rights
     */
    public abstract boolean isRoot(@NotNull User user);

    /**
     * Returns users of specified range sorted by id
     * @param offset offset
     * @param limit limit
     * @return list of found users
     * @throws DBException is db logic fail occurred
     * @throws IllegalArgumentException if offset < 0 or limit <= 0
     */
    public abstract List<User> findUsersPage(long offset, int limit) throws DBException;

    /**
     * Returns count of users in db
     * @return count of users
     */
    public abstract long getUsersCount() throws DBException;

    /**
     * Updates user status
     * @param userId user id
     * @param status new user status
     * @return true if status was successfully updated, false otherwise
     * @throws DBException if this UserDao throws this exception
     * @throws java.util.NoSuchElementException if there is no user with id userId
     */
    public abstract boolean updateUserStatus(long userId, User.Status status) throws DBException;

    /**
     * Returns set of roles that may be created by the user
     * @param user user of role admin or higher
     * @return set of roles that may be created by the user
     * @throws IllegalArgumentException if user's role is neither ADMIN nor ROOT
     */
    public abstract @NotNull Set<User.Role> rolesAllowedForCreation(@NotNull User user);
}
