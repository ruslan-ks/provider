package com.provider.service;

import com.provider.dao.ConnectionSupplier;
import com.provider.dao.DaoFactory;
import com.provider.dao.exception.DBException;
import com.provider.dao.postgres.PostgresDaoFactory;
import com.provider.entity.EntityFactory;
import com.provider.entity.SimpleEntityFactory;
import com.provider.validation.ValidatorFactory;
import com.provider.validation.ValidatorFactoryImpl;

public abstract class AbstractService {
    protected final DaoFactory daoFactory;

    protected final ConnectionSupplier connectionSupplier;

    protected final ValidatorFactory validatorFactory;

    protected final EntityFactory entityFactory;

    /*
    More Constructors(and corresponding newInstance() methods)
    taking DaoFactory, ConnectionSupplier and so on could be provided in the future.
     */

    protected AbstractService() throws DBException {
        daoFactory = PostgresDaoFactory.newInstance();
        connectionSupplier = daoFactory.newConnectionSupplier();
        validatorFactory = ValidatorFactoryImpl.newInstance();
        entityFactory = SimpleEntityFactory.newInstance();
    }
}
