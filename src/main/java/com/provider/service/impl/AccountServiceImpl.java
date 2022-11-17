package com.provider.service.impl;

import com.provider.dao.DaoFactory;
import com.provider.dao.UserAccountDao;
import com.provider.dao.exception.DBException;
import com.provider.entity.Currency;
import com.provider.entity.user.User;
import com.provider.entity.user.UserAccount;
import com.provider.service.AccountService;
import com.provider.service.exception.ValidationException;
import com.provider.validation.MoneyValidator;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AccountServiceImpl extends AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    AccountServiceImpl() throws DBException {}

    public AccountServiceImpl(@NotNull DaoFactory daoFactory) throws DBException {
        super(daoFactory);
    }

    @Override
    public @NotNull List<UserAccount> findUserAccounts(long userId) throws DBException {
        try (var connection = connectionSupplier.get()) {
            final UserAccountDao userAccountDao = daoFactory.newUserAccountDao();
            userAccountDao.setConnection(connection);
            return userAccountDao.findAll(userId);
        } catch (SQLException ex) {
            logFailedToCloseConnection(logger, ex);
            throw new DBException(ex);
        }
    }

    @Override
    public boolean replenish(@NotNull UserAccount account, @NotNull BigDecimal amount)
            throws DBException, ValidationException {
        final MoneyValidator moneyValidator = validatorFactory.getMoneyValidator();
        if (!moneyValidator.isPositiveAmount(amount)) {
            throw new ValidationException("Invalid amount: " + amount);
        }
        account.replenish(amount);
        try (var connection = connectionSupplier.get()) {
            final UserAccountDao userAccountDao = daoFactory.newUserAccountDao();
            userAccountDao.setConnection(connection);
            return userAccountDao.update(account);
        } catch (SQLException ex) {
            logFailedToCloseConnection(logger, ex);
            throw new DBException(ex);
        }
    }

    @Override
    public boolean isUserAccount(@NotNull UserAccount account, @NotNull User user) {
        return account.getUserId() == user.getId();
    }

    @Override
    public @NotNull Optional<UserAccount> findUserAccount(@NotNull User user, @NotNull Currency accountCurrency)
            throws DBException {
        return findUserAccounts(user.getId()).stream()
                .filter(acc -> acc.getCurrency().equals(accountCurrency))
                .findAny();
    }

    @Override
    public @NotNull UserAccount findUserAccount(@NotNull User user) throws DBException {
        return findUserAccount(user, Currency.USD)
                .orElseThrow(() -> new RuntimeException("Failed to find user USD account! user: " + user));
    }
}
