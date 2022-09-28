package com.provider.service;

import com.provider.dao.exception.DBException;
import org.jetbrains.annotations.NotNull;

public class ServiceFactoryImpl implements ServiceFactory {
    ServiceFactoryImpl() {}

    public static ServiceFactoryImpl newInstance() {
        return new ServiceFactoryImpl();
    }

    @Override
    public @NotNull UserService getUserService() throws DBException {
        return new UserServiceImpl();
    }

    @Override
    public @NotNull AccountService getAccountService() throws DBException {
        return new AccountServiceImpl();
    }
}
