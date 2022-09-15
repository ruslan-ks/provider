package com.provider.dao.postgres;

import com.provider.dao.DaoFactory;
import com.provider.dao.UserDao;
import com.provider.dao.UserPasswordDao;
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
}
