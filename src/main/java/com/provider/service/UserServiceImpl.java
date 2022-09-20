package com.provider.service;

import com.provider.dao.*;
import com.provider.dao.exception.DBException;
import com.provider.dao.postgres.PostgresDaoFactory;
import com.provider.dao.transaction.Transaction;
import com.provider.entity.Currency;
import com.provider.entity.user.User;
import com.provider.entity.user.UserAccount;
import com.provider.entity.user.UserPassword;
import com.provider.entity.user.UserStatus;
import com.provider.entity.user.impl.UserAccountImpl;
import com.provider.entity.user.impl.UserStatusImpl;
import com.provider.entity.user.hashing.PasswordHashing;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * User service implementation. Contains business logic and data access methods for User.
 */
public class UserServiceImpl implements UserService {
    private final DaoFactory daoFactory;
    private final ConnectionSupplier connectionSupplier;

    /*
    More Constructors(and corresponding newInstance() methods)
    taking DaoFactory and ConnectionSupplier may be provided in the future.
     */

    private UserServiceImpl() throws DBException {
        daoFactory = PostgresDaoFactory.newInstance();
        connectionSupplier = daoFactory.newConnectionSupplier();
    }

    public static UserServiceImpl newInstance() throws DBException {
        return new UserServiceImpl();
    }

    @Override
    public @NotNull Optional<User> findUserByLogin(@NotNull String login) throws DBException {
        try (var connection = connectionSupplier.get()) {
            final UserDao userDao = daoFactory.newUserDao();
            userDao.setConnection(connection);
            return userDao.findByLogin(login);
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    @Override
    public @NotNull Optional<UserPassword> findUserPassword(long userId) throws DBException {
        try (var connection = connectionSupplier.get()) {
            final UserPasswordDao userPasswordDao = daoFactory.newUserPasswordDao();
            userPasswordDao.setConnection(connection);
            return userPasswordDao.findByKey(userId);
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    @Override
    public boolean insertUser(@NotNull User user, @NotNull UserPassword userPassword) throws DBException {
        final UserDao userDao = daoFactory.newUserDao();
        final UserPasswordDao userPasswordDao = daoFactory.newUserPasswordDao();
        final UserStatusDao userStatusDao = daoFactory.newUserStatusDao();
        final UserAccountDao userAccountDao =  daoFactory.newUserAccountDao();
        try (var transaction = Transaction.of(connectionSupplier.get(), userDao, userPasswordDao,
                userStatusDao, userAccountDao)) {
            final boolean userInserted;
            final boolean passwordInserted;
            final boolean statusInserted;
            final boolean accountInserted;
            try {
                userInserted = userDao.insert(user);

                userPassword.setUserId(user.getId());
                passwordInserted = userPasswordDao.insert(userPassword);

                final UserStatus userStatus = UserStatusImpl.newInstance(user.getId(), UserStatus.Status.ACTIVE,
                        "new", Instant.now());
                statusInserted = userStatusDao.insert(userStatus);

                final UserAccount userAccount = UserAccountImpl.newInstance(0, user.getId(), Currency.USD);
                accountInserted = userAccountDao.insert(userAccount);

                transaction.commit();
            } catch (Throwable ex) {
                transaction.rollback();
                throw ex;
            }
            return userInserted && passwordInserted && statusInserted && accountInserted;
        }
    }

    @Override
    public @NotNull Optional<User> authenticate(@NotNull String login, @NotNull String password)
            throws DBException {
        final Optional<User> foundUser = findUserByLogin(login);
        if (foundUser.isEmpty()) {
            return Optional.empty();
        }
        final Optional<UserPassword> foundPasswordOptional = findUserPassword(foundUser.get().getId());
        if (foundPasswordOptional.isEmpty()) {
            return Optional.empty();
        }
        final UserPassword foundPassword = foundPasswordOptional.get();
        final PasswordHashing passwordHashing = PasswordHashing.getInstance(foundPassword.getHashMethod());
        final UserPassword hashedUserProvidedPassword = passwordHashing.hash(password, foundPassword.getSalt());

        // No need to check salt equality of two passwords, cause the second password was generated using the salt
        // obtained from the first one
        if (foundPassword.getHash().equals(hashedUserProvidedPassword.getHash())) {
            return foundUser;
        }
        return Optional.empty();
    }

    @Override
    public @NotNull List<UserAccount> findAllUserAccounts(long userId) throws DBException {
        try (var connection = connectionSupplier.get()) {
            final UserAccountDao userAccountDao = daoFactory.newUserAccountDao();
            userAccountDao.setConnection(connection);
            return userAccountDao.findAll(userId);
        } catch (SQLException ex) {
            throw new DBException();
        }
    }
}
