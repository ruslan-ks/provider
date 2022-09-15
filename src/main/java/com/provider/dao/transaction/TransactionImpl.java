package com.provider.dao.transaction;

import com.provider.dao.EntityDao;
import com.provider.dao.exception.DBException;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionImpl implements Transaction {
    private final Connection connection;
    private int previousTransactionIsolation;

    private TransactionImpl(@NotNull Connection connection, @NotNull EntityDao<?, ?>... entityDaos)
            throws DBException {
        this.connection = connection;
        tryConfigureConnection();
        for (var dao : entityDaos) {
            dao.setConnection(this.connection);
        }
    }

    public static TransactionImpl newInstance(@NotNull Connection connection,
                                              @NotNull EntityDao<?, ?>... entityDaos)
            throws DBException {
        return new TransactionImpl(connection, entityDaos);
    }

    private void tryConfigureConnection() throws DBException {
        try {
            connection.setAutoCommit(false);
            previousTransactionIsolation = connection.getTransactionIsolation();
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    @Override
    public void commit() throws DBException {
        try {
            connection.commit();
        } catch (SQLException ex) {
            throw new DBException();
        }
    }

    @Override
    public void rollback() throws DBException {
        try {
            connection.rollback();
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    @Override
    public void close() throws DBException {
        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(previousTransactionIsolation);
            connection.close();
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }
}
