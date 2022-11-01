package com.provider.dao.postgres;

import com.provider.dao.UserAccountDao;
import com.provider.dao.exception.DBException;
import com.provider.entity.Currency;
import com.provider.entity.user.UserAccount;
import com.provider.util.Checks;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostgresUserAccountDao extends UserAccountDao {
    private static final Logger logger = LoggerFactory.getLogger(PostgresUserAccountDao.class);

    PostgresUserAccountDao() {}

    private static final String SQL_FIND_BY_ID =
            "SELECT " +
                    "id AS user_account_id, " +
                    "user_id AS user_id, " +
                    "currency AS user_account_currency, " +
                    "amount AS user_account_amount " +
            "FROM user_accounts " +
            "WHERE id = ?";

    @Override
    public @NotNull Optional<UserAccount> findByKey(@NotNull Long id) throws DBException {
        return findByKey(SQL_FIND_BY_ID, id);
    }

    private static final String SQL_INSERT = "INSERT INTO user_accounts(user_id, currency, amount) VALUES (?, ?, ?)";

    @Override
    public boolean insert(@NotNull UserAccount userAccount) throws DBException {
        try (var preparedStatement = connection.prepareStatement(SQL_INSERT,
                Statement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            preparedStatement.setLong(i++, userAccount.getUserId());
            preparedStatement.setString(i++, userAccount.getCurrency().name());
            preparedStatement.setBigDecimal(i, userAccount.getAmount());
            final boolean inserted = preparedStatement.executeUpdate() > 0;

            final ResultSet keysResultSet = preparedStatement.getGeneratedKeys();
            if (inserted) {
                if (keysResultSet.next()) {
                    userAccount.setId(keysResultSet.getLong(1));
                } else {
                    throw new DBException("Failed to get generated keys after updating user_accounts table");
                }
            }
            return inserted;
        } catch (SQLException ex) {
            logger.error("Failed to insert user account: {}", userAccount);
            logger.error("Failed to insert user account!", ex);
            throw new DBException(ex);
        }
    }

    private static final String SQL_FIND_BY_USER_ID =
            "SELECT " +
                    "id AS user_account_id, " +
                    "user_id AS user_id, " +
                    "currency AS user_account_currency, " +
                    "amount AS user_account_amount " +
            "FROM user_accounts " +
            "WHERE user_id = ?";

    @Override
    public @NotNull List<UserAccount> findAll(long userId) throws DBException {
        Checks.throwIfInvalidId(userId);
        try (var preparedStatement = connection.prepareStatement(SQL_FIND_BY_USER_ID)) {
            preparedStatement.setLong(1, userId);
            final ResultSet resultSet = preparedStatement.executeQuery();
            return fetchAll(resultSet);
        } catch (SQLException ex) {
            logger.error("Failed to find user accounts! user id: {}", userId);
            logger.error("Failed to find user accounts!", ex);
            throw new DBException(ex);
        }
    }

    private static final String SQL_UPDATE = "UPDATE user_accounts SET amount = ? WHERE id = ?";

    @Override
    public boolean update(@NotNull UserAccount account) throws DBException {
        Checks.throwIfInvalidId(account.getId());
        try (var preparedStatement = connection.prepareStatement(SQL_UPDATE)) {
            int i = 1;
            preparedStatement.setBigDecimal(i++, account.getAmount());
            preparedStatement.setLong(i, account.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logger.error("Failed to update user account: {}", account);
            logger.error("Failed to update user account!", ex);
            throw new DBException(ex);
        }
    }

    @Override
    protected @NotNull UserAccount fetchOne(@NotNull ResultSet resultSet) throws DBException {
        try {
            final long id = resultSet.getLong("user_account_id");
            final long userId = resultSet.getLong("user_id");
            final String currencyString = resultSet.getString("user_account_currency");
            final BigDecimal amount = resultSet.getBigDecimal("user_account_amount");
            return entityFactory.newUserAccount(id, userId, Currency.valueOf(currencyString), amount);
        } catch (SQLException ex) {
            logger.error("Failed to fetch user account data!", ex);
            throw new DBException(ex);
        }
    }

    private @NotNull List<UserAccount> fetchAll(@NotNull ResultSet resultSet) throws DBException, SQLException {
        final List<UserAccount> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(fetchOne(resultSet));
        }
        return list;
    }
}
