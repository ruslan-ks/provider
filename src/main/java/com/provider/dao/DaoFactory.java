package com.provider.dao;

import com.provider.dao.exception.DBException;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract factory for daos creation
 */
public interface DaoFactory {
    /**
     * Creates new ConnectionSupplier for created daos
     * @return new ConnectionSupplier
     */
    @NotNull ConnectionSupplier newConnectionSupplier() throws DBException;

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

    @NotNull UserAccountDao newUserAccountDao();

    @NotNull ServiceDao newServiceDao();

    @NotNull TariffDao newTariffDao();
}
