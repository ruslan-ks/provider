package com.provider.dao;

import com.provider.entity.EntityFactory;
import com.provider.entity.SimpleEntityFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractDaoTest {
    protected static final EntityFactory entityFactory = SimpleEntityFactory.newInstance();

    protected static Connection connection;

    @AfterAll
    static void afterAll() throws SQLException {
        connection.close();
    }

    @AfterEach
    public void clearAllTables() throws SQLException {
        deleteAllUsers();
        clearAllTariffs();
        clearAllServices();
    }

    private void deleteAllUsers() throws SQLException {
        try (var statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM users WHERE TRUE");
        }
    }

    private void clearAllServices() throws SQLException {
        try (var statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM services WHERE TRUE");
        }
    }

    private void clearAllTariffs() throws SQLException {
        try (var statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM tariffs WHERE TRUE");
        }
    }
}
