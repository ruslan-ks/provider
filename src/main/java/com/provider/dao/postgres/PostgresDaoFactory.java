package com.provider.dao.postgres;

import com.provider.dao.*;
import com.provider.dao.exception.DBException;
import org.jetbrains.annotations.NotNull;

public class PostgresDaoFactory implements DaoFactory {
    public static DaoFactory newInstance() {
        return new PostgresDaoFactory();
    }

    @Override
    public @NotNull UserDao newUserDao() {
        return new PostgresUserDao();
    }

    @Override
    public @NotNull UserPasswordDao newUserPasswordDao() {
        return new PostgresUserPasswordDao();
    }

    @Override
    public @NotNull UserStatusDao newUserStatusDao() {
        return new PostgresUserStatusDao();
    }

    @Override
    public @NotNull ConnectionSupplier newConnectionSupplier() throws DBException {
        return new PostgresConnectionSupplier();
    }
}
