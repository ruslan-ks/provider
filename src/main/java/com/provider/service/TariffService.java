package com.provider.service;

import com.provider.dao.exception.DBException;
import com.provider.entity.dto.TariffDto;
import com.provider.entity.product.Service;
import com.provider.entity.product.Tariff;
import com.provider.entity.product.TariffDuration;
import com.provider.service.exception.ValidationException;
import com.provider.sorting.TariffOrderRule;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Service layer class for tariffs and services
 * @see com.provider.dao.TariffDao
 * @see com.provider.dao.ServiceDao
 * @see com.provider.dao.TariffDurationDao
 */
public interface TariffService {
    /**
     * Returns list of all services translated according to the provided locale
     * @param locale service translation locale
     * @return list of services; services content may be translated or not depending on where the translation is present
     * @throws DBException if {@link com.provider.dao.ServiceDao} throws DBException
     */
    @NotNull List<Service> findAllServices(@NotNull String locale) throws DBException;

    /**
     * Inserts service to the db and sets id on service object
     * @param service service to be inserted
     * @return true if service was inserted successfully, false otherwise
     * @throws DBException if {@link com.provider.dao.ServiceDao} throws DBException
     */
    boolean insertService(@NotNull Service service) throws DBException, ValidationException;

    /**
     * Returns page of TariffDto objects
     * @param offset offset
     * @param limit limit
     * @param orderRules order rules that define the order of tariffs
     * @param locale desired locale
     * @param serviceIds if not empty, only tariffs containing specific services will be selected;
     *                   if empty, tariffs are selected independent of their services
     * @param activeOnly if true, only tariffs with 'ACTIVE' status will be selected,
     *                   otherwise all tariffs will be selected
     * @return {@code List<TariffDto>} containing all the tariffs found
     * @throws DBException if {@link com.provider.dao.TariffDao} throws DBException
     */
    @NotNull List<TariffDto> findTariffsPage(long offset,
                                             int limit,
                                             @NotNull String locale,
                                             @NotNull Set<TariffOrderRule> orderRules,
                                             @NotNull Set<Integer> serviceIds,
                                             boolean activeOnly) throws DBException;

    /**
     * Returns optional containing tariff if found
     * @param tariffId tariff id
     * @return optional containing tariff object if found, empty optional otherwise
     */
    @NotNull Optional<Tariff> findTariffById(int tariffId) throws DBException;

    /**
     * Returns tariff count
     * @return tariff count
     * @throws DBException if {@link com.provider.dao.TariffDao} throws DBException
     */
    int countAllTariffs() throws DBException;

    /**
     * Return count of tariffs that that hav status ACTIVE
     * @return count of tariffs that that hav status ACTIVE
     * @throws DBException if {@link com.provider.dao.TariffDao} throws DBException
     */
    int countActiveTariffs() throws DBException;

    /**
     * Inserts tariff, tariff_duration and service ids to the corresponding tables. Returns true if successfully inserted.
     * Changes tariff id and tariffDuration's tariffId setting the db-generated value
     * @param tariff tariff to be inserted
     * @param tariffDuration tariff duration to be inserted
     * @param serviceIds tariff service ids to be added to the new tariff
     * @return true in case of success, false if insertion failed, for example if one of services does not exist
     * @throws ValidationException if tariff or tariffDuration has invalid property values
     * @throws IllegalArgumentException if {@code serviceIds.isEmpty() == true}
     */
    boolean insertTariff(@NotNull Tariff tariff, @NotNull TariffDuration tariffDuration, @NotNull Set<Integer> serviceIds)
            throws ValidationException, DBException;

    /**
     * Returns Map where key - service, value - service tariffs count; if there is no localization provided,
     * the default version will be returned
     * @param locale desired locale
     * @param activeOnly find active tariffs only if true, all tariffs otherwise
     * @return Map of services and their tariff occasions count
     * @throws DBException if {@link com.provider.dao.ServiceDao} throws it
     */
    @NotNull Map<Service, Integer> findAllServicesTariffsCount(@NotNull String locale, boolean activeOnly)
            throws DBException;
}
