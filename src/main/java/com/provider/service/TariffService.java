package com.provider.service;

import com.provider.dao.exception.DBException;
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
     * @throws DBException if dao throws db exception
     * @see com.provider.dao.ServiceDao
     */
    @NotNull List<Service> findAllServices(@NotNull String locale) throws DBException;

    /**
     * Inserts service to the db and sets id on service object
     * @param service service to be inserted
     * @return true if service was inserted successfully, false otherwise
     * @throws DBException if dao throws db exception
     * @see com.provider.dao.ServiceDao
     */
    boolean insertService(@NotNull Service service) throws DBException, InvalidPropertyException;
}
