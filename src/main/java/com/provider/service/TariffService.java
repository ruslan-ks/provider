package com.provider.service;

import com.provider.dao.exception.DBException;
import com.provider.entity.product.Service;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface TariffService {
    /**
     * Returns list of all services translated according to the provided locale
     * @param locale service translation locale
     * @return list of services; services content may be translated or not depending on where the translation is present
     */
    @NotNull List<Service> findAllServices(@NotNull String locale) throws DBException;
}
