package com.provider.service;

import com.provider.dao.*;
import com.provider.dao.exception.DBException;
import com.provider.dao.transaction.Transaction;
import com.provider.entity.Currency;
import com.provider.entity.user.User;
import com.provider.entity.user.UserAccount;
import com.provider.entity.user.UserPassword;
import com.provider.entity.user.impl.UserAccountImpl;
import com.provider.entity.user.hashing.PasswordHashing;
import com.provider.service.exception.InvalidPropertyException;
import com.provider.validation.UserValidator;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

/**
 * User service implementation. Contains business logic and data access methods for User.
 */
public class UserServiceImpl extends AbstractService implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserValidator userValidator = validatorFactory.getUserValidator();

    UserServiceImpl() throws DBException {}

    public static UserServiceImpl newInstance() throws DBException {
        return new UserServiceImpl();
    }

    @Override
    public @NotNull Optional<User> findUserById(long id) throws DBException {
        try (var connection = connectionSupplier.get()) {
            final UserDao userDao = daoFactory.newUserDao();
            userDao.setConnection(connection);
            return userDao.findByKey(id);
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
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
    public boolean insertUser(@NotNull User user, @NotNull String password)
            throws DBException, InvalidPropertyException {
        throwIfAnyIsInvalid(user, password);

        final UserDao userDao = daoFactory.newUserDao();
        final UserPasswordDao userPasswordDao = daoFactory.newUserPasswordDao();
        final UserAccountDao userAccountDao =  daoFactory.newUserAccountDao();
        try (var transaction = Transaction.of(connectionSupplier.get(), userDao, userPasswordDao,
                userAccountDao)) {
            try {
                final boolean userInserted = userDao.insert(user);

                final UserPassword userPassword = UserPassword.hash(password);
                userPassword.setUserId(user.getId());
                final boolean passwordInserted = userPasswordDao.insert(userPassword);

                final UserAccount userAccount = UserAccountImpl.of(0, user.getId(), Currency.USD);
                final boolean accountInserted = userAccountDao.insert(userAccount);

                transaction.commit();
                return userInserted && passwordInserted && accountInserted;
            } catch (Throwable ex) {
                transaction.rollback();
                logger.error("Couldn't execute transaction {}", transaction, ex);
                throw ex;
            }
        }
    }

    private void throwIfAnyIsInvalid(@NotNull User user, @NotNull String password) throws InvalidPropertyException {
        if (!userValidator.isValidLogin(user.getLogin())
                || !userValidator.isValidPassword(password)
                || !userValidator.isValidName(user.getName())
                || !userValidator.isValidSurname(user.getSurname())
                || !userValidator.isValidPhone(user.getPhone())) {
            throw new InvalidPropertyException();
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
    public boolean isActiveUser(@NotNull User user) {
        return user.getStatus().equals(User.Status.ACTIVE);
    }

    @Override
    public boolean hasAdminRights(@NotNull User user) {
        return List.of(User.Role.ADMIN, User.Role.ROOT).contains(user.getRole());
    }

    @Override
    public boolean hasRootRights(@NotNull User user) {
        return user.getRole().equals(User.Role.ROOT);
    }

    @Override
    public List<User> findUsersRange(long offset, int limit) throws DBException {
        if (offset < 0 || limit <= 0) {
            throw new IllegalArgumentException("Invalid range: offset: " + offset + ", limit: " + limit);
        }
        try (var connection = connectionSupplier.get()) {
            final UserDao userDao = daoFactory.newUserDao();
            userDao.setConnection(connection);
            return userDao.findRange(offset, limit);
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    @Override
    public long getUsersCount() throws DBException {
        try (var connection = connectionSupplier.get()) {
            final UserDao userDao = daoFactory.newUserDao();
            userDao.setConnection(connection);
            return userDao.getUserCount();
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    @Override
    public boolean updateUserStatus(long userId, User.Status status) throws DBException{
        try (var connection = connectionSupplier.get()) {
            final UserDao userDao = daoFactory.newUserDao();
            userDao.setConnection(connection);
            final User user = findUserById(userId).orElseThrow(NoSuchElementException::new);
            if (user.getStatus().equals(status)) {
                return false;
            }
            user.setStatus(status);
            return userDao.update(user);
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    @Override
    public @NotNull Set<User.Role> rolesAllowedForCreation(@NotNull User user) {
        switch (user.getRole()) {
            case ROOT:
                return Set.of(User.Role.ADMIN, User.Role.MEMBER);
            case ADMIN:
                return Set.of(User.Role.MEMBER);
        }
        throw new IllegalArgumentException();
    }
}
