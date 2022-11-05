package com.provider.dao.postgres;

import com.provider.dao.ConnectionSupplier;
import com.provider.dao.ServiceDao;
import com.provider.dao.ServiceDaoTestBase;
import com.provider.dao.exception.DBException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class PostgresDaoTest extends ServiceDaoTestBase {
    private static final Logger logger = LoggerFactory.getLogger(PostgresDaoTest.class);

    private Connection connection;

    private final ConnectionSupplier connectionSupplier = new PostgresTestConnectionSupplier();

    @BeforeEach
    public void setupConnection() throws DBException {
        connection = connectionSupplier.get();
        logger.debug("Setting up db connection");
    }

    @AfterEach
    public void cleanUpAndCloseConnection() throws SQLException {
        clearAllServices();
        connection.close();
        logger.debug("Closing db connection");
    }

    private void clearAllServices() throws SQLException {
        try (var statement = getConnection().createStatement()) {
            statement.executeUpdate("DELETE FROM services WHERE TRUE");
        }
    }

    @Override
    protected ServiceDao getServiceDao() {
        return new PostgresServiceDao();
    }

    @Override
    protected Connection getConnection() {
        return connection;
    }
}
