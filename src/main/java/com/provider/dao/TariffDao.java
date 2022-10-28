package com.provider.dao;

import com.provider.dao.exception.DBException;
import com.provider.entity.dto.TariffDto;
import com.provider.entity.product.Service;
import com.provider.entity.product.Tariff;
import com.provider.sorting.TariffOrderRule;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Tariff dao
 */
public abstract class TariffDao extends EntityDao<Integer, Tariff> {
    /**
     * Returns Optional containing TariffDto if one is present, empty Optional otherwise.<br>
     * If there is no translation for the specified {@code locale}, the default one will be returned.
     * @param id tariff id
     * @param locale desired locale
     * @return Optional containing TariffDto if one is present, empty Optional otherwise
     * @throws DBException if SQLException occurred
     */
    public abstract @NotNull Optional<TariffDto> findFullInfoByKey(int id, @NotNull String locale)
            throws DBException;

    // TODO: javadoc for new params
    /**
     * Returns DTO containing Tariff, TariffDuration and {@code List<Service>}.
     * If there is no translation for the specified {@code locale}, the default one will be returned.
     * @param offset offset
     * @param limit limit
     * @param locale desired locale
     * @return List of TariffDto
     * @throws DBException if SQLException occurred
     */
    public abstract @NotNull List<TariffDto> findFullInfoPage(long offset, int limit,
                                                              @NotNull String locale,
                                                              @NotNull Set<TariffOrderRule> orderRules,
                                                              @NotNull Set<Integer> serviceIds,
                                                              boolean activeOnly)
            throws DBException;

    /**
     * Adds tariff service link
     * @param tariffId tariff id
     * @param serviceIds service ids to be added to the tariff
     * @return true if db changes were made successfully
     * @throws DBException if SQLException occurred
     * @throws IllegalArgumentException if {@code tariffId <= 0}
     */
    public abstract boolean addServices(int tariffId, @NotNull Set<Integer> serviceIds) throws DBException;

    /**
     * Returns list of tariff services
     * @param tariffId tariff id
     * @return {@code List<Service>} belonging to the tariff with tariffId
     * @throws DBException if {@link ServiceDao} throws DBException
     * @throws IllegalArgumentException if tariffId <= 0
     */
    public abstract List<Service> findTariffServices(int tariffId, @NotNull String locale) throws DBException;

    /**
     * Returns records count
     * @return count of all tariffs count
     * @throws DBException if SQLException occurred
     */
    public abstract int countAll() throws DBException;

    /**
     * Returns ACTIVE tariff records count
     * @return all tariffs count
     * @throws DBException if SQLException occurred
     */
    public abstract int countActive() throws DBException;
}
