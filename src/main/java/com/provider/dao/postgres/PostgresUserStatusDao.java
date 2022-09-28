package com.provider.dao.postgres;

import com.provider.dao.UserStatusDao;
import com.provider.dao.exception.DBException;
import com.provider.entity.user.UserStatus;
import com.provider.entity.user.impl.UserStatusImpl;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

public class PostgresUserStatusDao extends UserStatusDao {

    PostgresUserStatusDao() {}

    @Override
    public @NotNull Optional<UserStatus> findByKey(@NotNull Integer userId) {
        throw new UnsupportedOperationException();
    }

    private final static String SQL_SELECT_CURRENT_BY_USER_ID =
            "SELECT " +
                    "user_id AS user_id, " +
                    "status AS status, " +
                    "comment AS comment, " +
                    "set_time AS set_time " +
            "FROM user_statuses " +
            "WHERE user_id = ?" +
            "ORDER BY set_time DESC " +
            "LIMIT 1";

    @Override
    public @NotNull Optional<UserStatus> findCurrentUserStatus(long userId) throws DBException {
        try (var preparedStatement = connection.prepareStatement(SQL_SELECT_CURRENT_BY_USER_ID)) {
            preparedStatement.setLong(1, userId);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(fetchUserStatus(resultSet));
            }
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
        return Optional.empty();
    }

    private static final String SQL_INSERT = "INSERT INTO user_statuses(user_id, status, comment, set_time) " +
            "VALUES (?, ?, ?, ?)";

    @Override
    public boolean insert(@NotNull UserStatus userStatus) throws DBException {
        try (var preparedStatement = connection.prepareStatement(SQL_INSERT)) {
            int i = 1;
            preparedStatement.setLong(i++, userStatus.getUserId());
            preparedStatement.setString(i++, userStatus.getStatus().name());
            preparedStatement.setString(i++, userStatus.getComment());
            preparedStatement.setObject(i, Timestamp.from(userStatus.getSetTime()));
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    /**
     * Fetch user status data
     * @param resultSet result set with cursor pointing to a user status data
     * @return UserStatus object containing resultSet data
     */
    private static @NotNull UserStatus fetchUserStatus(@NotNull ResultSet resultSet) throws SQLException {
        final long userId = resultSet.getLong("user_id");
        final String status = resultSet.getString("status");
        final String comment = resultSet.getString("comment");
        // getObject("set_time", Instant.class);
        final Instant setTime = resultSet.getTimestamp("set_time").toInstant();
        return UserStatusImpl.newInstance(userId, UserStatus.Status.valueOf(status), comment, setTime);
    }
}
