package com.provider.service;

import com.provider.dao.exception.DBException;
import org.jetbrains.annotations.NotNull;

/**
 * Service factory, creates different service objects
 */
public interface ServiceFactory {
    /**
     * Returns UserService object
     * @return UserService object
     */
    @NotNull UserService getUserService() throws DBException;

    /**
     * Returns AccountService object
     * @return AccountService object
     */
    @NotNull AccountService getAccountService() throws DBException;

    /**
     * Return TariffService object
     * @return TariffService object
     */
    @NotNull TariffService getTariffService() throws DBException;
}
