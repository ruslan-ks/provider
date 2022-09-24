package com.provider.service;

import com.provider.dao.ConnectionSupplier;
import com.provider.dao.DaoFactory;
import com.provider.dao.exception.DBException;
import com.provider.dao.postgres.PostgresDaoFactory;

public abstract class AbstractService {
    protected final DaoFactory daoFactory;
    protected final ConnectionSupplier connectionSupplier;

    /*
    More Constructors(and corresponding newInstance() methods)
    taking DaoFactory and ConnectionSupplier may be provided in the future.
     */

    protected AbstractService() throws DBException {
        daoFactory = PostgresDaoFactory.newInstance();
        connectionSupplier = daoFactory.newConnectionSupplier();
    }
}
