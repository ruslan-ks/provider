package com.provider.dao;

import com.provider.dao.exception.DBException;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract factory for daos creation
 */
public interface DaoFactory {
    /**
     * Creates new UserDao
     * @return new UserDao
     */
    @NotNull UserDao newUserDao();

    /**
     * Creates new UserPasswordDao
     * @return new UserPasswordDao
     */
    @NotNull UserPasswordDao newUserPasswordDao();

    /**
     * Creates new UserStatusDao
     * @return new UserStatusDao
     */
    @NotNull UserStatusDao newUserStatusDao();

    /**
     * Creates new ConnectionSupplier for created daos
     * @return new ConnectionSupplier
     */
    @NotNull ConnectionSupplier newConnectionSupplier() throws DBException;
}
