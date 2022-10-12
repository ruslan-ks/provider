package com.provider.dao;

import com.provider.dao.exception.DBException;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Test utility class. Provides useful methods used by different dao tests.
 * Does not contain any tests.
 */
public class DaoTestUtil {
    private DaoTestUtil() {}

    /**
     * Returns ConnectionSupplier that can be used for db testing
     * @return ConnectionSupplier for database testing
     */
    public static ConnectionSupplier getPostgresConnectionSupplier() {
        return new ConnectionSupplier() {
            static final String URL = "jdbc:postgresql://127.0.0.1:5432/provider";

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
