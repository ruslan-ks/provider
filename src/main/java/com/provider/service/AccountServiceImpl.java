package com.provider.service;

import com.provider.dao.UserAccountDao;
import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import com.provider.entity.user.UserAccount;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AccountServiceImpl extends AbstractService implements AccountService {
    AccountServiceImpl() throws DBException {}

    public static AccountServiceImpl newInstance() throws DBException {
        return new AccountServiceImpl();
    }

    @Override
    public @NotNull List<UserAccount> findUserAccounts(long userId) throws DBException {
        try (var connection = connectionSupplier.get()) {
            final UserAccountDao userAccountDao = daoFactory.newUserAccountDao();
            userAccountDao.setConnection(connection);
            return userAccountDao.findAll(userId);
        } catch (SQLException ex) {
            throw new DBException();
        }
    }

    @Override
    public @NotNull Optional<UserAccount> findAccount(long accountId) throws DBException {
        try (var connection = connectionSupplier.get()) {
            final UserAccountDao userAccountDao = daoFactory.newUserAccountDao();
            userAccountDao.setConnection(connection);
            return userAccountDao.findByKey(accountId);
        } catch (SQLException ex) {
            throw new DBException();
        }
    }

    @Override
    public boolean replenish(@NotNull UserAccount account, @NotNull BigDecimal amount) throws DBException {
        account.replenish(amount);
        try (var connection = connectionSupplier.get()) {
            final UserAccountDao userAccountDao = daoFactory.newUserAccountDao();
            userAccountDao.setConnection(connection);
            return userAccountDao.update(account);
        } catch (SQLException ex) {
            throw new DBException();
        }
    }

    @Override
    public boolean isUserAccount(@NotNull UserAccount account, @NotNull User user) {
        return account.getUserId() == user.getId();
    }
}