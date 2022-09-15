package com.provider.dao.transaction;

import com.provider.dao.EntityDao;
import com.provider.dao.exception.DBException;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

/**
 * Manages database transaction
 */
public interface Transaction extends AutoCloseable {
    /**
     * Convenience method for transaction creation
     * @param connection active connection, will be used by daos of entityDaos
     * @param entityDaos transaction daos
     * @return new instance of TransactionImpl
     */
    static @NotNull Transaction of(@NotNull Connection connection, @NotNull EntityDao<?, ?>... entityDaos)
            throws DBException {
        return TransactionImpl.newInstance(connection, entityDaos);
    }

    /**
     * Calls commit() on the Connection object
     */
    void commit() throws DBException;

    /**
     * Calls rollback() on the Connection object
     */
    void rollback() throws DBException;

    /**
     * Close underlying connection
     * @throws DBException if exception occurred when trying to release resources
     */
    void close() throws DBException;
}
