package com.provider.dao.postgres;

import com.provider.dao.ConnectionSupplier;
import com.provider.dao.exception.DBException;
import org.jetbrains.annotations.NotNull;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class PostgresConnectionSupplier implements ConnectionSupplier {
    private final Context context;

    private PostgresConnectionSupplier() throws DBException {
        try {
            context = new InitialContext();
        } catch (NamingException ex) {
            throw new DBException(ex);
        }
    }

    public static PostgresConnectionSupplier newInstance() throws DBException {
        return new PostgresConnectionSupplier();
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
            throw new DBException(ex);
        }
    }
}
