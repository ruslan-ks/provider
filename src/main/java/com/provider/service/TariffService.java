package com.provider.service;

import com.provider.dao.exception.DBException;
import com.provider.entity.dto.TariffDto;
import com.provider.entity.product.Service;
import com.provider.service.exception.InvalidPropertyException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
    boolean insertService(@NotNull Service service) throws DBException, InvalidPropertyException;

    /**
     * Returns page of TariffDto objects
     * @param offset offset
     * @param limit limit
     * @return {@code List<TariffDto>} containing all the tariffs found
     * @throws DBException if {@link com.provider.dao.TariffDao} throws DBException
     */
    @NotNull List<TariffDto> findTariffsPage(long offset, int limit, @NotNull String locale) throws DBException;

    /**
     * Returns tariff count
     * @return tariff count
     * @throws DBException if {@link com.provider.dao.TariffDao} throws DBException
     */
    int countAllTariffs() throws DBException;
}
