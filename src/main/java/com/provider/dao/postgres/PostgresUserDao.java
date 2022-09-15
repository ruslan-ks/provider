package com.provider.dao.postgres;

import com.provider.dao.UserDao;
import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import com.provider.entity.user.UserImpl;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class PostgresUserDao extends UserDao {
    @Override
    public @NotNull Optional<User> findByKey(@NotNull Long key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull List<User> findAll() {
        throw new UnsupportedOperationException();
    }

    private static final String SQL_INSERT = "INSERT INTO users(login, role, name, surname, phone) VALUES" +
            "(?, ?, ?, ?, ?)";

    @Override
    public boolean insert(@NotNull User user) throws DBException {
        try (var statement = connection.prepareStatement(SQL_INSERT,
                Statement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            statement.setString(i++, user.getLogin());
            statement.setString(i++, user.getRole().name());
            statement.setString(i++, user.getName());
            statement.setString(i++, user.getSurname());
            statement.setString(i, user.getPhone());

            final int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                final ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                } else {
                    throw new DBException("Table users has been updated, but no generated keys returned");
                }
                return true;
            }
            return false;
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    private static final String SQL_FIND_BY_LOGIN =
            "SELECT " +
                    "id AS id, " +
                    "login AS login, " +
                    "name AS name, " +
                    "surname AS surname, " +
                    "phone AS phone, " +
                    "role AS role " +
            "FROM users " +
                    "WHERE login = ?";

    @Override
    public Optional<User> findByLogin(@NotNull String login) throws DBException {
        try (var statement = connection.prepareStatement(SQL_FIND_BY_LOGIN)) {
            statement.setString(1, login);
            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(fetchUser(resultSet));
            }
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
        return Optional.empty();
    }

    private static @NotNull User fetchUser(@NotNull ResultSet resultSet) throws SQLException {
        final long id = resultSet.getLong("id");
        final String name = resultSet.getString("name");
        final String surname = resultSet.getString("surname");
        final String login = resultSet.getString("login");
        final String phone = resultSet.getString("phone");
        final String role = resultSet.getString("role");

        return newUserInstance(id, name, surname, login, phone, User.Role.valueOf(role));
    }

    private static @NotNull User newUserInstance(long id, @NotNull String name, @NotNull String surname, @NotNull String login,
                                                 @NotNull String phone, @NotNull User.Role role) {
        return UserImpl.newInstance(id, name, surname, login, phone, role);
    }
}
