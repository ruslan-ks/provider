package com.provider.dao.transaction;

import com.provider.dao.EntityDao;
import com.provider.dao.exception.DBException;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TransactionImpl implements Transaction {
    private final Connection connection;
    private int previousTransactionIsolation;
    private final List<EntityDao<?, ?>> entityDaoList;

    private TransactionImpl(@NotNull Connection connection, @NotNull EntityDao<?, ?>... entityDaos)
            throws DBException {
        this.connection = connection;
        tryConfigureConnection();
        this.entityDaoList = List.of(entityDaos);
        this.entityDaoList.forEach(dao -> dao.setConnection(this.connection));
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
            throw new DBException(ex);
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
            connection.setAutoCommit(true);
            connection.setTransactionIsolation(previousTransactionIsolation);
            connection.close();
            entityDaoList.forEach(EntityDao::resetConnection);
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    @Override
    public String toString() {
        return "TransactionImpl{" +
                "connection=" + connection +
                ", previousTransactionIsolation=" + previousTransactionIsolation +
                ", entityDaoList=" + entityDaoList +
                '}';
    }
}
