package com.provider.dao.postgres;

import com.provider.dao.UserAccountDao;
import com.provider.dao.exception.DBException;
import com.provider.entity.Currency;
import com.provider.entity.user.UserAccount;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostgresUserAccountDao extends UserAccountDao {
    PostgresUserAccountDao() {}

    private static final String SQL_FIND_BY_ID =
            "SELECT " +
                    "id AS id, " +
                    "user_id AS user_id, " +
                    "currency AS currency, " +
                    "amount AS amount " +
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
            throw new DBException();
        }
    }

    private static final String SQL_FIND_BY_USER_ID =
            "SELECT " +
                    "id AS id, " +
                    "user_id AS user_id, " +
                    "currency AS currency, " +
                    "amount AS amount " +
            "FROM user_accounts " +
            "WHERE user_id = ?";

    @Override
    public @NotNull List<UserAccount> findAll(long userId) throws DBException {
        if (userId == 0) {
            throw new IllegalArgumentException("Illegal userId: userId == 0");
        }
        try (var preparedStatement = connection.prepareStatement(SQL_FIND_BY_USER_ID)) {
            preparedStatement.setLong(1, userId);
            final ResultSet resultSet = preparedStatement.executeQuery();
            return fetchAll(resultSet);
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    private static final String SQL_UPDATE = "UPDATE user_accounts SET amount = ? WHERE id = ?";

    @Override
    public boolean update(@NotNull UserAccount account) throws DBException {
        if (account.getId() == 0) {
            throw new IllegalArgumentException("account(" + account + ") id == 0");
        }
        try (var preparedStatement = connection.prepareStatement(SQL_UPDATE)) {
            int i = 1;
            preparedStatement.setBigDecimal(i++, account.getAmount());
            preparedStatement.setLong(i, account.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new DBException();
        }
    }

    @Override
    protected UserAccount fetchOne(@NotNull ResultSet resultSet) throws DBException {
        try {
            final long id = resultSet.getLong("id");
            final long userId = resultSet.getLong("user_id");
            final String currencyString = resultSet.getString("currency");
            final BigDecimal amount = resultSet.getBigDecimal("amount");
            return entityFactory.newUserAccount(id, userId, Currency.valueOf(currencyString), amount);
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    private @NotNull List<UserAccount> fetchAll(@NotNull ResultSet resultSet) throws DBException {
        final List<UserAccount> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                list.add(fetchOne(resultSet));
            }
        } catch (SQLException ex) {
            throw new DBException(ex);
        }

        return list;
    }
}
