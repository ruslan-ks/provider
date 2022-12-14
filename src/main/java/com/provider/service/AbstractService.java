package com.provider.service;

import com.provider.dao.ConnectionSupplier;
import com.provider.dao.DaoFactory;
import com.provider.dao.exception.DBException;
import com.provider.dao.postgres.PostgresDaoFactory;
import com.provider.entity.EntityFactory;
import com.provider.entity.SimpleEntityFactory;
import com.provider.validation.ValidatorFactory;
import com.provider.validation.ValidatorFactoryImpl;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.sql.SQLException;

public abstract class AbstractService {
    protected DaoFactory daoFactory;

    protected ConnectionSupplier connectionSupplier;

    protected final ValidatorFactory validatorFactory;

    protected final EntityFactory entityFactory;

    protected AbstractService() throws DBException {
        this(PostgresDaoFactory.newInstance());
    }

    protected AbstractService(@NotNull DaoFactory daoFactory) throws DBException {
        this.daoFactory = daoFactory;
        connectionSupplier = daoFactory.getConnectionSupplier();
        validatorFactory = ValidatorFactoryImpl.newInstance();
        entityFactory = SimpleEntityFactory.newInstance();
    }

    protected void logFailedToCloseConnection(@NotNull Logger logger, @NotNull SQLException ex) {
        logger.error("Failed to close connection", ex);
    }
}
