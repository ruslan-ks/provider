package com.provider.dao;

import com.provider.dao.exception.DBException;
import com.provider.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

/**
 * DAO pattern main ancestor. All DAOs must extend this class.
 * Before performing DAO operations setConnection must be called.
 * DAOs must never close obtained connection object.
 * @param <K> table primary key
 * @param <T> entity class
 */
public abstract class EntityDao<K, T extends Entity> {
    protected Connection connection;

    public void setConnection(@NotNull Connection connection) {
        this.connection = connection;
    }

    public abstract @NotNull Optional<T> findByKey(@NotNull K key) throws DBException;

    public abstract @NotNull List<T> findAll() throws DBException;

    /**
     * Inserts entity. Updates object's id field according to id generated by the database.
     * @param entity - entity to be inserted
     * @return true if any changes to db were made
     * @throws DBException if SQLException occurred
     */
    public abstract boolean insert(@NotNull T entity) throws DBException;
}
