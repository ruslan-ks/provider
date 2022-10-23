package com.provider.dao.postgres;

import com.provider.dao.*;
import com.provider.dao.exception.DBException;
import org.jetbrains.annotations.NotNull;

public class PostgresDaoFactory implements DaoFactory {
    public static DaoFactory newInstance() {
        return new PostgresDaoFactory();
    }

    @Override
    public @NotNull ConnectionSupplier newConnectionSupplier() throws DBException {
        return new PostgresConnectionSupplier();
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
    public @NotNull UserAccountDao newUserAccountDao() {
        return new PostgresUserAccountDao();
    }

    @Override
    public @NotNull ServiceDao newServiceDao() {
        return new PostgresServiceDao();
    }

    @Override
    public @NotNull TariffDao newTariffDao() {
        return new PostgresTariffDao();
    }

    @Override
    public @NotNull TariffDurationDao newTariffDurationDao() {
        return new PostgresTariffDurationDao();
    }

    @Override
    public @NotNull SubscriptionDao newSubscriptionDao() {
        return new PostgresSubscriptionDao();
    }
}
