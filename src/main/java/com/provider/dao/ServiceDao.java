package com.provider.dao;

import com.provider.dao.exception.DBException;
import com.provider.entity.product.Service;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public abstract class ServiceDao extends EntityDao<Integer, Service> {
    /**
     * Returns all existing services
     * @return list of services obtained via 'SELECT *'
     * @throws DBException if SQLException occurred
     */
    public abstract @NotNull List<Service> findAll() throws DBException;

    /**
     * Returns all existing services translated to the specified language if translation exists
     * @param locale desired language
     * @return list of services translated to the specified language if translation exists
     * @throws DBException if SQLException occurred
     */
    public abstract @NotNull List<Service> findAll(@NotNull String locale) throws DBException;

    /**
     * Returns Optional containing localized service;
     * If there is no localized version, but the default one exists, default version will be returned
     * If there is neither default nor localized version, returns empty optional
     * @param key service id
     * @param locale desired localization language
     * @return Optional containing localized service;
     * If there is no localized version, but the default one exists, default version will be returned
     * If there is neither default nor localized version, returns empty optional
     */
    public abstract @NotNull Optional<Service> findByKey(@NotNull Integer key, @NotNull String locale)
            throws DBException;

    /**
     * Inserts content to a localized services table. Does not insert records to a primary services table.
     * Does not check whether primary service table record exists or not
     * @param service service
     * @param locale localization language
     * @return true if record was inserted successfully
     * @throws DBException if SQLException occurred(also if there is no primary service record);
     */
    public abstract boolean insertTranslation(@NotNull Service service, @NotNull String locale) throws DBException;
}
