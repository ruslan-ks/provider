package com.provider.dao.postgres;

import com.provider.dao.UserDao;
import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import com.provider.util.Checks;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostgresUserDao extends UserDao {
    private static final Logger logger = LoggerFactory.getLogger(PostgresUserDao.class);

    PostgresUserDao() {}

    private static final String SQL_USER_FIELDS = "id AS id, " +
            "login AS login, " +
            "name AS name, " +
            "surname AS surname, " +
            "phone AS phone, " +
            "role AS role, " +
            "status AS status ";

    private static final String SQL_FIND_BY_ID =
            "SELECT " +
                    SQL_USER_FIELDS +
            "FROM users " +
                    "WHERE id = ?";

    @Override
    public @NotNull Optional<User> findByKey(@NotNull Long key) throws DBException {
        return findByKey(SQL_FIND_BY_ID, key);
    }

    private static final String SQL_INSERT = "INSERT INTO users(login, role, name, surname, phone, status) VALUES" +
            "(?, ?, ?, ?, ?, ?)";

    @Override
    public boolean insert(@NotNull User user) throws DBException {
        try (var statement = connection.prepareStatement(SQL_INSERT,
                Statement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            statement.setString(i++, user.getLogin());
            statement.setString(i++, user.getRole().name());
            statement.setString(i++, user.getName());
            statement.setString(i++, user.getSurname());
            statement.setString(i++, user.getPhone());
            statement.setString(i, user.getStatus().name());

            final int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                final ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                    return true;
                }
                throw new DBException("Table users has been updated, but no generated keys returned");
            }
        } catch (SQLException ex) {
            logger.error("Failed to insert user: {}", user);
            logger.error("Failed to insert user!", ex);
            throw new DBException(ex);
        }
        return false;
    }

    private static final String SQL_FIND_BY_LOGIN =
            "SELECT " +
                    SQL_USER_FIELDS +
            "FROM users " +
                    "WHERE login = ?";

    @Override
    public Optional<User> findByLogin(@NotNull String login) throws DBException {
        try (var statement = connection.prepareStatement(SQL_FIND_BY_LOGIN)) {
            statement.setString(1, login);
            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(fetchOne(resultSet));
            }
        } catch (SQLException ex) {
            logger.error("Failed to find user by login! Login: {}", login);
            logger.error("Failed to find user by login!", ex);
            throw new DBException(ex);
        }
        return Optional.empty();
    }

    private static final String SQL_FIND_PAGE =
            "SELECT " +
                    SQL_USER_FIELDS +
            "FROM users " +
            "ORDER BY id " +
            "OFFSET ? " +
            "LIMIT ? ";

    @Override
    public List<User> findPage(long offset, int limit) throws DBException {
        Checks.throwIfInvalidOffsetOrLimit(offset, limit);
        try (var preparedStatement = connection.prepareStatement(SQL_FIND_PAGE)) {
            int i = 1;
            preparedStatement.setLong(i++, offset);
            preparedStatement.setInt(i, limit);
            final ResultSet resultSet = preparedStatement.executeQuery();
            return fetchAll(resultSet);
        } catch (SQLException ex) {
            logger.error("Failed to find users page!", ex);
            throw new DBException(ex);
        }
    }

    private static final String SQL_COUNT_ALL = "SELECT COUNT(id) FROM users";

    @Override
    public long getUserCount() throws DBException {
        try (var statement = connection.createStatement();
             var resultSet = statement.executeQuery(SQL_COUNT_ALL)) {
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
        } catch (SQLException ex) {
            logger.error("Failed to get user count!", ex);
            throw new DBException(ex);
        }
        throw new DBException("Couldn't get users count");
    }

    private static final String SQL_UPDATE =
            "UPDATE users " +
            "SET " +
                    "name = ?, " +
                    "surname = ?, " +
                    "phone = ?, " +
                    "role = ?, " +
                    "status = ? " +
            "WHERE id = ?";

    @Override
    public boolean update(@NotNull User user) throws DBException {
        Checks.throwIfInvalidId(user.getId());
        try (var preparedStatement = connection.prepareStatement(SQL_UPDATE)) {
            int i = 1;
            preparedStatement.setString(i++, user.getName());
            preparedStatement.setString(i++, user.getSurname());
            preparedStatement.setString(i++, user.getPhone());
            preparedStatement.setString(i++, user.getRole().name());
            preparedStatement.setString(i++, user.getStatus().name());
            preparedStatement.setLong(i, user.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logger.error("Failed to update user: {}", user);
            logger.error("Failed to update user!", ex);
            throw new DBException(ex);
        }
    }

    @Override
    protected @NotNull User fetchOne(@NotNull ResultSet resultSet) throws DBException {
        try {
            final long id = resultSet.getLong("id");
            final String name = resultSet.getString("name");
            final String surname = resultSet.getString("surname");
            final String login = resultSet.getString("login");
            final String phone = resultSet.getString("phone");
            final User.Role role = User.Role.valueOf(resultSet.getString("role"));
            final User.Status status = User.Status.valueOf(resultSet.getString("status"));
            return entityFactory.newUser(id, name, surname, login, phone, role, status);
        } catch (SQLException ex) {
            logger.error("Failed to fetch user data!", ex);
            throw new DBException(ex);
        }
    }

    private @NotNull List<User> fetchAll(@NotNull ResultSet resultSet) throws DBException, SQLException {
        final List<User> userList = new ArrayList<>();
        while (resultSet.next()) {
            userList.add(fetchOne(resultSet));
        }
        return userList;
    }
}
