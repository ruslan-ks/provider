package com.provider.dao.postgres;

import com.provider.dao.SubscriptionDao;
import com.provider.dao.exception.DBException;
import com.provider.entity.product.Subscription;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostgresSubscriptionDao extends SubscriptionDao {
    private static final Logger logger = LoggerFactory.getLogger(PostgresSubscriptionDao.class);

    private static final String SQL_FIND_BY_ID =
            "SELECT " +
                    "id AS subscription_id, " +
                    "user_account_id AS subscription_user_account_id, " +
                    "tariff_id AS subscription_tariff_id, " +
                    "start_time AS subscription_start_time, " +
                    "last_payment_time AS subscription_last_payment_time, " +
                    "status AS subscription_status " +
            "FROM subscriptions " +
            "WHERE id = ?";

    @Override
    public @NotNull Optional<Subscription> findByKey(@NotNull Integer key) throws DBException {
        return findByKey(SQL_FIND_BY_ID, key);
    }

    private static final String SQL_INSERT =
            "INSERT INTO subscriptions(user_account_id, tariff_id, start_time, last_payment_time, status) " +
            "VALUES (?, ?, ?, ?, ?)";

    @Override
    public boolean insert(@NotNull Subscription subscription) throws DBException {
        try (var preparedStatement = connection.prepareStatement(SQL_INSERT,
                Statement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            preparedStatement.setLong(i++, subscription.getUserAccountId());
            preparedStatement.setInt(i++, subscription.getTariffId());
            preparedStatement.setTimestamp(i++, Timestamp.from(subscription.getStartTime()));
            preparedStatement.setTimestamp(i++, Timestamp.from(subscription.getLastPaymentTime()));
            preparedStatement.setString(i, subscription.getStatus().name());
            final int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                final ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    subscription.setId(generatedKeys.getLong(1));
                    return true;
                }
                logger.error("Failed to obtain generated keys! Failed to obtain generated keys after " +
                        "insertion! Subscription: {}", subscription);
                throw new DBException("Failed to obtain generated keys! Failed to obtain generated keys after " +
                        "insertion! Subscription: " + subscription);
            }
        } catch (SQLException ex) {
            logger.error("Failed to insert subscription", ex);
            throw new DBException(ex);
        }
        return false;
    }

    private static final String SQL_FIND_BY_USER_ACCOUNT =
            "SELECT " +
                    "id AS subscription_id, " +
                    "user_account_id AS subscription_user_account_id, " +
                    "tariff_id AS subscription_tariff_id, " +
                    "start_time AS subscription_start_time, " +
                    "last_payment_time AS subscription_last_payment_time, " +
                    "status AS subscription_status " +
            "FROM subscriptions " +
            "WHERE user_account_id = ?";

    @Override
    public List<Subscription> findSubscriptions(long userAccountId) throws DBException {
        if (userAccountId <= 0) {
            throw new IllegalArgumentException("User account id <= 0: userAccountId = " + userAccountId);
        }
        try (var preparedStatement = connection.prepareStatement(SQL_FIND_BY_USER_ACCOUNT)) {
            preparedStatement.setLong(1, userAccountId);
            final ResultSet resultSet = preparedStatement.executeQuery();
            return fetchAll(resultSet);
        } catch (SQLException ex) {
            logger.error("Failed to find subscriptions by user account id!", ex);
            throw new DBException(ex);
        }
    }

    @Override
    protected @NotNull Subscription fetchOne(@NotNull ResultSet resultSet) throws DBException {
        try {
            final long id = resultSet.getInt("subscription_id");
            final long userAccountId = resultSet.getLong("subscription_user_account_id");
            final int tariffId = resultSet.getInt("subscription_tariff_id");
            final Instant startTime = resultSet.getTimestamp("subscription_start_time").toInstant();
            final Instant lastPaymentTime = resultSet.getTimestamp("subscription_last_payment_time").toInstant();
            final Subscription.Status status = Subscription.Status.valueOf(resultSet.getString("subscription_status"));
            return entityFactory.newSubscription(id, userAccountId, tariffId, startTime, lastPaymentTime, status);
        } catch (SQLException ex) {
            logger.error("Failed to fetch subscription data!", ex);
            throw new DBException(ex);
        }
    }

    private List<Subscription> fetchAll(@NotNull ResultSet resultSet) throws SQLException, DBException {
        final List<Subscription> subscriptionList = new ArrayList<>();
        while (resultSet.next()) {
            subscriptionList.add(fetchOne(resultSet));
        }
        return subscriptionList;
    }
}
