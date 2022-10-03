package com.provider.dao;

import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * User data access object
 */
public abstract class UserDao extends EntityDao<Long, User> {
    /**
     * Returns Optional of user with specified login
     * @param login user login
     * @return Optional of user with specified login if found, empty Optional otherwise
     * @throws DBException if db error occurred
     */
    public abstract Optional<User> findByLogin(@NotNull String login) throws DBException;

    /**
     * Returns users of specified range sorted by id
     * @param offset offset
     * @param limit limit
     * @return list of found users
     * @throws DBException is db logic fail occurred
     * @throws IllegalArgumentException if offset < 0 or limit <= 0
     */
    public abstract List<User> findRange(long offset, int limit) throws DBException;

    /**
     * Returns count of user records in db
     * @return count of users in db
     */
    public abstract long getUserCount() throws DBException;

    /**
     * Updates user data in db
     * @param user user object that will be used to update db
     * @return true if db changes were made
     */
    public abstract boolean update(@NotNull User user) throws DBException;
}
