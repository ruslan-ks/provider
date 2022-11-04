package com.provider.dao.postgres;

import com.provider.dao.ConnectionSupplier;
import com.provider.dao.exception.DBException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class PostgresDaoTestBase {
    private static final Logger logger = LoggerFactory.getLogger(PostgresDaoTestBase.class);

    protected Connection connection;

    @BeforeEach
    public void setupConnection() throws DBException {
        connection = getConnectionSupplier().get();
        logger.info("Setting up db connection");
    }

    @AfterEach
    public void closeConnection() throws SQLException {
        connection.close();
        logger.info("Closing db connection");
    }

    /**
     * Returns ConnectionSupplier that can be used for db testing
     * @return ConnectionSupplier for database testing
     */
    private ConnectionSupplier getConnectionSupplier() {
        return new ConnectionSupplier() {
            static final String URL = "jdbc:postgresql://127.0.0.1:5432/provider_test";

            @Override
            public @NotNull Connection get() throws DBException {
                try {
                    return DriverManager.getConnection(URL);
                } catch (SQLException ex) {
                    throw new DBException(ex);
                }
            }
        };
    }
}
