package com.provider.dao.postgres;

import com.provider.dao.ConnectionSupplier;
import com.provider.dao.exception.DBException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class PostgresConnectionSupplier implements ConnectionSupplier {
    private static final Logger logger = LoggerFactory.getLogger(PostgresConnectionSupplier.class);

    private static final String DATASOURCE_NAME = "java:/comp/env/jdbc/postgres/provider";

    private static DataSource dataSource;

    static {
        try {
            final Context context = new InitialContext();
            dataSource = (DataSource) context.lookup(DATASOURCE_NAME);
            if (dataSource == null) {
                logger.error("Failed to obtain DataSource object!");
            }
        } catch (NamingException ex) {
            logger.error("Failed to obtain DataSource!", ex);
        }
    }

    @Override
    public @NotNull Connection get() throws DBException {
        if (dataSource == null) {
            throw new DBException("dataSource == null");
        }
        try {
            return dataSource.getConnection();
        } catch (SQLException ex) {
            logger.error("Failed to obtain connection!", ex);
            throw new DBException(ex);
        }
    }
}
