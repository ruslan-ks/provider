package com.provider.dao.postgres;

import com.provider.dao.ConnectionSupplier;
import com.provider.dao.exception.DBException;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Postgres test database connection supplier<br>
 * Supplies connection to the database that has structure similar to the production postgres db
 */
public class PostgresTestConnectionSupplier implements ConnectionSupplier {
    static final String URL = "jdbc:postgresql://127.0.0.1:5432/provider_test";

    @Override
    public @NotNull Connection get() throws DBException {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }
}
