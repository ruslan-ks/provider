package com.provider.dao.postgres;

import com.provider.dao.*;
import com.provider.dao.exception.DBException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.SQLException;

public class PostgresUserDaoTest extends AbstractUserDaoTest {
    private final ConnectionSupplier connectionSupplier = new PostgresTestConnectionSupplier();
    private Connection connection;

    @Override
    protected Connection getConnection() {
        return connection;
    }

    @Override
    protected UserDao getUserDao() {
        return new PostgresUserDao();
    }

    @BeforeEach
    public void setupConnection() throws DBException {
        connection = connectionSupplier.get();
    }

    @AfterEach
    public void cleanUpAndCloseConnection() throws SQLException {
        deleteAllUsers();
        connection.close();
    }

    private void deleteAllUsers() throws SQLException {
        try (var statement = getConnection().createStatement()) {
            statement.executeUpdate("DELETE FROM users WHERE TRUE");
        }
    }
}
