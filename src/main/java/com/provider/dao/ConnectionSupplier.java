package com.provider.dao;

import com.provider.dao.exception.DBException;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

/**
 * Database connection supplier
 */
public interface ConnectionSupplier {
    /**
     * Returns connection to the database<br>
     * After obtaining connection via this method, <strong>client code MUST EXPLICITLY CLOSE THE CONNECTION
     * via .close() method call</strong>
     * @return db connection
     * @throws DBException if connection cannot be obtained
     */
    @NotNull Connection get() throws DBException;
}
