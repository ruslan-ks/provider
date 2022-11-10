package com.provider.service.impl;

import com.provider.dao.exception.DBException;
import com.provider.service.*;
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

    @Override
    public @NotNull TariffService getTariffService() throws DBException {
        return new TariffServiceImpl();
    }

    @Override
    public @NotNull SubscriptionService getSubscriptionService() throws DBException {
        return new SubscriptionServiceImpl();
    }
}
