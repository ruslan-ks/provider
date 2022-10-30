package com.provider.dao.postgres;

import com.provider.dao.SubscriptionDao;
import com.provider.dao.exception.DBException;
import com.provider.entity.dto.*;
import com.provider.entity.product.Service;
import com.provider.entity.product.Subscription;
import com.provider.entity.product.Tariff;
import com.provider.entity.product.TariffDuration;
import com.provider.util.Checks;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

public class PostgresSubscriptionDao extends SubscriptionDao {
    private static final Logger logger = LoggerFactory.getLogger(PostgresSubscriptionDao.class);

    private static final String SQL_FIND_BY_ID =
            "SELECT " +
                    "id AS subscription_id, " +
                    "user_account_id AS user_account_id, " +
                    "tariff_id AS tariff_id, " +
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
                    "user_account_id AS user_account_id, " +
                    "tariff_id AS tariff_id, " +
                    "start_time AS subscription_start_time, " +
                    "last_payment_time AS subscription_last_payment_time, " +
                    "status AS subscription_status " +
            "FROM subscriptions " +
            "WHERE user_account_id = ?";

    @Override
    public List<Subscription> findSubscriptions(long userAccountId) throws DBException {
        Checks.throwIfInvalidId(userAccountId);
        try (var preparedStatement = connection.prepareStatement(SQL_FIND_BY_USER_ACCOUNT)) {
            preparedStatement.setLong(1, userAccountId);
            final ResultSet resultSet = preparedStatement.executeQuery();
            return fetchAll(resultSet);
        } catch (SQLException ex) {
            logger.error("Failed to find subscriptions by user account id!", ex);
            throw new DBException(ex);
        }
    }

    private static final String SQL_FIND_FULL_INFO_BY_USER_ACCOUNT =
            "SELECT " +
                    "sub.id AS subscription_id, " +
                    "sub.user_account_id AS user_account_id, " +
                    "sub.start_time AS subscription_start_time, " +
                    "sub.last_payment_time AS subscription_last_payment_time, " +
                    "sub.status AS subscription_status, " +
                    "t.id AS tariff_id, " +
                    "COALESCE(tt.title, t.title) AS tariff_title, " +
                    "COALESCE(tt.description, t.description) AS tariff_description, " +
                    "t.status AS tariff_status, " +
                    "t.usd_price AS tariff_usd_price, " +
                    "t.image_file_name AS tariff_image_file_name, " +
                    "td.months AS tariff_duration_months, " +
                    "td.minutes AS tariff_duration_minutes, " +
                    "s.id AS service_id, " +
                    "COALESCE(st.name, s.name) AS service_name, " +
                    "COALESCE(st.description, s.description) AS service_description " +
            "FROM subscriptions sub " +
            "INNER JOIN tariffs t " +
                    "ON t.id = sub.tariff_id " +
            "INNER JOIN tariff_durations td " +
                    "ON td.tariff_id = t.id " +
            "INNER JOIN tariff_services ts " +
                    "ON ts.tariff_id = t.id " +
            "INNER JOIN services s " +
                    "ON s.id = ts.service_id " +
            "LEFT JOIN tariff_translations tt " +
                    "ON tt.tariff_id = t.id AND tt.locale = ? " +
            "LEFT JOIN service_translations st " +
                    "ON st.service_id = s.id AND st.locale = ? " +
            "WHERE sub.user_account_id = ? " +
            "ORDER BY sub.last_payment_time DESC";

    @Override
    public List<SubscriptionTariffDto> findSubscriptionsFullInfo(long userAccountId, @NotNull String locale) throws DBException {
        Checks.throwIfInvalidId(userAccountId);
        try (var preparedStatement = connection.prepareStatement(SQL_FIND_FULL_INFO_BY_USER_ACCOUNT)) {
            int i = 1;
            preparedStatement.setString(i++, locale);
            preparedStatement.setString(i++, locale);
            preparedStatement.setLong(i, userAccountId);
            final ResultSet resultSet = preparedStatement.executeQuery();

            final PostgresServiceDao serviceDao = new PostgresServiceDao();
            final PostgresTariffDurationDao tariffDurationDao = new PostgresTariffDurationDao();
            final PostgresTariffDao tariffDao = new PostgresTariffDao();

            final Map<Long, SubscriptionTariffDto> subscriptionTariffDtoMap = new LinkedHashMap<>(); // key - subscription id
            final Map<Integer, List<Service>> tariffServicesMap = new HashMap<>(); // key - tariff id, value - list of service
            while (resultSet.next()) {
                final Subscription subscription = fetchOne(resultSet);

                // computeIfAbsent() could be applied, but fetchOne() may throw DBException, and it would look scattered
                if (!subscriptionTariffDtoMap.containsKey(subscription.getId())) {
                    final TariffDto tariffDto = SimpleTariffDto.of(
                            tariffDao.fetchOne(resultSet), tariffDurationDao.fetchOne(resultSet));
                    final SubscriptionTariffDto subscriptionTariffDto = SimpleSubscriptionTariffDto.of(
                            subscription, tariffDto);
                    subscriptionTariffDtoMap.put(subscription.getId(), subscriptionTariffDto);
                }
                tariffServicesMap.computeIfAbsent(subscription.getTariffId(), id -> new ArrayList<>())
                        .add(serviceDao.fetchOne(resultSet));
            }
            return subscriptionTariffDtoMap.values().stream()
                    .peek(stDto -> stDto.getTariffDto()
                            .addServices(tariffServicesMap.get(stDto.getSubscription().getTariffId())))
                    .toList();
        } catch (SQLException ex) {
            logger.error("Failed to obtain user subscriptions!", ex);
            throw new DBException(ex);
        }
    }

    private static final String SQL_UPDATE = "UPDATE subscriptions SET last_payment_time = ?, status = ? WHERE id = ?";

    @Override
    public boolean update(@NotNull Subscription subscription) throws DBException {
        Checks.throwIfInvalidId(subscription.getId());
        try (var preparedStatement = connection.prepareStatement(SQL_UPDATE)) {
            int i = 1;
            preparedStatement.setTimestamp(i++, Timestamp.from(subscription.getLastPaymentTime()));
            preparedStatement.setString(i++, subscription.getStatus().name());
            preparedStatement.setLong(i, subscription.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logger.error("Failed to update subscription!", ex);
            throw new DBException(ex);
        }
    }

    private static final String SQL_FIND_EXPIRED_ACTIVE_SUBSCRIPTIONS =
            """ 
            SELECT
                *
            FROM (
                SELECT
                    s.id AS subscription_id,
                    s.status AS subscription_status,
                    s.user_account_id AS user_account_id,
                    s.start_time AS subscription_start_time,
                    s.last_payment_time AS subscription_last_payment_time,
                    t.id AS tariff_id,
                    t.title AS tariff_title,
                    t.usd_price AS tariff_usd_price,
                    t.description AS tariff_description,
                    t.status AS tariff_status,
                    t.image_file_name AS tariff_image_file_name,
                    td.months AS tariff_duration_months,
                    td.minutes AS tariff_duration_minutes,
                    s.last_payment_time + INTERVAL '1' MONTH * td.months + INTERVAL '1' MINUTE * td.minutes AS renewal_time
                FROM
                    subscriptions s
                INNER JOIN tariffs t
                    ON t.id = s.tariff_id
                INNER JOIN tariff_durations td
                    ON td.tariff_id = t.id
                ) tbl
            WHERE subscription_status = 'ACTIVE' AND renewal_time < (now() AT TIME ZONE 'UTC')
            """;

    @Override
    public List<SubscriptionDto> findAllExpiredActiveSubscriptions() throws DBException {
        final var tariffDao = new PostgresTariffDao();
        final var tariffDurationDao = new PostgresTariffDurationDao();
        try (var statement = connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery(SQL_FIND_EXPIRED_ACTIVE_SUBSCRIPTIONS);
            final List<SubscriptionDto> subscriptionDtoList = new ArrayList<>();
            while (resultSet.next()) {
                final Subscription subscription = fetchOne(resultSet);
                final Tariff tariff = tariffDao.fetchOne(resultSet);
                final TariffDuration tariffDuration = tariffDurationDao.fetchOne(resultSet);
                subscriptionDtoList.add(SimpleSubscriptionDto.of(tariff, tariffDuration, subscription));
            }
            return subscriptionDtoList;
        } catch (SQLException ex) {
            logger.error("Failed to obtain expired active subscriptions!", ex);
            throw new DBException(ex);
        }
    }

    @Override
    protected @NotNull Subscription fetchOne(@NotNull ResultSet resultSet) throws DBException {
        try {
            final long id = resultSet.getInt("subscription_id");
            final long userAccountId = resultSet.getLong("user_account_id");
            final int tariffId = resultSet.getInt("tariff_id");
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
