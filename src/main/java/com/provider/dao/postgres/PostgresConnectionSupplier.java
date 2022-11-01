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

    private final Context context;

    PostgresConnectionSupplier() throws DBException {
        try {
            context = new InitialContext();
        } catch (NamingException ex) {
            logger.error("Naming exception!", ex);
            throw new DBException(ex);
        }
    }

    @Override
    public @NotNull Connection get() throws DBException {
        final DataSource dataSource;
        try {
            dataSource = (DataSource) context.lookup( "java:/comp/env/jdbc/postgres/provider");
            if (dataSource == null) {
                throw new DBException("Couldn't get DataSource object from context");
            }
            return dataSource.getConnection();
        } catch (NamingException | SQLException ex) {
            logger.error("Failed to obtain connection!", ex);
            throw new DBException(ex);
        }
    }
}
