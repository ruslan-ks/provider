package com.provider.service;

import com.provider.dao.*;
import com.provider.dao.exception.DBException;
import com.provider.dao.transaction.Transaction;
import com.provider.entity.Currency;
import com.provider.entity.user.User;
import com.provider.entity.user.UserAccount;
import com.provider.entity.user.UserPassword;
import com.provider.entity.user.hashing.PasswordHashing;
import com.provider.service.exception.ValidationException;
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
            logFailedToCloseConnection(logger, ex);
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
            logFailedToCloseConnection(logger, ex);
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
            logFailedToCloseConnection(logger, ex);
            throw new DBException(ex);
        }
    }

    @Override
    public boolean insertUser(@NotNull User user, @NotNull String password)
            throws DBException, ValidationException {
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

                final UserAccount userAccount = entityFactory.newUserAccount(0, user.getId(), Currency.USD);
                final boolean accountInserted = userAccountDao.insert(userAccount);

                if (userInserted && passwordInserted && accountInserted) {
                    transaction.commit();
                    return true;
                }
            } catch (Throwable ex) {
                logger.error("Failed to execute transaction: {}", transaction);
                logger.error("Failed to insert user: {}", user);
                logger.error("Failed to insert user!", ex);
                transaction.rollback();
                throw ex;
            }
            transaction.rollback();
        }
        return false;
    }

    private void throwIfAnyIsInvalid(@NotNull User user, @NotNull String password) throws ValidationException {
        if (!userValidator.isValidLogin(user.getLogin())
                || !userValidator.isValidPassword(password)
                || !userValidator.isValidName(user.getName())
                || !userValidator.isValidSurname(user.getSurname())
                || !userValidator.isValidPhone(user.getPhone())) {
            throw new ValidationException();
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
        final UserPassword foundPassword = foundPasswordOptional
                .orElseThrow(() -> new RuntimeException("Password not found! User: " + foundUser.get()));

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
    public List<User> findUsersPage(long offset, int limit) throws DBException {
        if (offset < 0 || limit <= 0) {
            throw new IllegalArgumentException("Invalid range: offset: " + offset + ", limit: " + limit);
        }
        try (var connection = connectionSupplier.get()) {
            final UserDao userDao = daoFactory.newUserDao();
            userDao.setConnection(connection);
            return userDao.findPage(offset, limit);
        } catch (SQLException ex) {
            logFailedToCloseConnection(logger, ex);
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
            logFailedToCloseConnection(logger, ex);
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
            logFailedToCloseConnection(logger, ex);
            throw new DBException(ex);
        }
    }

    @Override
    public @NotNull Set<User.Role> rolesAllowedForCreation(@NotNull User user) {
        return switch (user.getRole()) {
            case ROOT -> Set.of(User.Role.ADMIN, User.Role.MEMBER);
            case ADMIN -> Set.of(User.Role.MEMBER);
            default -> throw new IllegalArgumentException("No roles are allowed to be created by '" + user.getRole() +
                    "'");
        };
    }
}
