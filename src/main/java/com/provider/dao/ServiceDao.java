package com.provider.dao;

import com.provider.dao.exception.DBException;
import com.provider.entity.product.Service;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

    /**
     * Counts for each service counts how many tariffs include this service.<br>
     * If there is no localization for specified locale, returns the default one
     * @param locale desired locale
     * @param activeOnly if true, only tariffs with 'ACTIVE' status will be count, otherwise all tariffs will be count
     * @return Map where key - service, value - amount of tariffs that include this service
     */
    public abstract @NotNull Map<Service, Integer> findAllServicesTariffsCount(@NotNull String locale,
                                                                               boolean activeOnly) throws DBException;

    /**
     * Returns count of distinct tariffs that include services with provided id
     * @param serviceIds service id set
     * @param activeOnly if true, only tariffs with 'ACTIVE' status are countered
     * @return count of distinct tariffs that include services with provided id
     * @throws IllegalArgumentException if {@code serviceIds} is empty or if at least one value is <= 0
     */
    public abstract int countDistinctTariffsIncludingServices(@NotNull Set<Integer> serviceIds, boolean activeOnly)
            throws DBException;
}
