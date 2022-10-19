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

public interface AccountService {
    /**
     * Returns list containing all accounts belonging to user with a specified id
     * @param userId user id
     * @return List containing all accounts belonging to user with a specified id
     */
    @NotNull List<UserAccount> findUserAccounts(long userId) throws DBException;

    /**
     * Returns optional containing UserAccount with specified id
     * @param accountId account id
     * @return Optional containing account with specified id if found, empty optional owherwise
     */
    @NotNull Optional<UserAccount> findAccount(long accountId) throws DBException;

    /**
     * Replenish account and save changes to db
     * @param account user account
     * @param amount money amount
     * @return true if db was changed
     */
    boolean replenish(@NotNull UserAccount account, @NotNull BigDecimal amount) throws DBException, ValidationException;

    /**
     * Returns true if account belongs to the specified user
     * @param account account
     * @param user user
     * @return true if account belongs to user
     */
    boolean isUserAccount(@NotNull UserAccount account, @NotNull User user) throws DBException;

    /**
     * Returns User account of specified currency.
     * For each currency User can have one and only one account
     * @param user user
     * @param accountCurrency account currency
     * @return Optional containing user account if found, empty Optional otherwise
     */
    @NotNull Optional<UserAccount> findUserAccount(@NotNull User user, @NotNull Currency accountCurrency) throws DBException;
}
