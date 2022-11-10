package com.provider.service;

import com.provider.dao.exception.DBException;
import com.provider.entity.Currency;
import com.provider.entity.user.User;
import com.provider.entity.user.UserAccount;
import com.provider.service.exception.ValidationException;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public abstract class AccountService extends AbstractService {
    protected AccountService() throws DBException {}

    /**
     * Returns list containing all accounts belonging to user with a specified id
     * @param userId user id
     * @return List containing all accounts belonging to user with a specified id
     */
    public abstract @NotNull List<UserAccount> findUserAccounts(long userId) throws DBException;

    /**
     * Returns optional containing UserAccount with specified id
     * @param accountId account id
     * @return Optional containing account with specified id if found, empty optional owherwise
     */
    public abstract @NotNull Optional<UserAccount> findAccount(long accountId) throws DBException;

    /**
     * Replenish account and save changes to db
     * @param account user account
     * @param amount money amount
     * @return true if db was changed
     */
    public abstract boolean replenish(@NotNull UserAccount account, @NotNull BigDecimal amount)
            throws DBException, ValidationException;

    /**
     * Returns true if account belongs to the specified user
     * @param account account
     * @param user user
     * @return true if account belongs to user
     */
    public abstract boolean isUserAccount(@NotNull UserAccount account, @NotNull User user) throws DBException;

    /**
     * Returns User account of specified currency.
     * For each currency User can have one and only one account
     * @param user user
     * @param accountCurrency account currency
     * @return Optional containing user account if found, empty Optional otherwise
     */
    public abstract @NotNull Optional<UserAccount> findUserAccount(@NotNull User user, @NotNull Currency accountCurrency)
            throws DBException;

    /**
     * Returns User USD account
     * @param user user
     * @return user account
     */
    public abstract @NotNull UserAccount findUserAccount(@NotNull User user) throws DBException;
}
