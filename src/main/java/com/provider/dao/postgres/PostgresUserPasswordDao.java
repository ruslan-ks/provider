package com.provider.dao.postgres;

import com.provider.dao.UserPasswordDao;
import com.provider.dao.exception.DBException;
import com.provider.entity.user.UserPassword;
import com.provider.entity.user.hashing.PasswordHashing;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class PostgresUserPasswordDao extends UserPasswordDao {
    private static final Logger logger = LoggerFactory.getLogger(PostgresUserPasswordDao.class);

    PostgresUserPasswordDao() {}

    private static final String SQL_INSERT = "INSERT INTO user_passwords(user_id, hash, salt, hash_method) VALUES " +
            "(?, ?, ?, ?)";

    @Override
    public boolean insert(@NotNull UserPassword userPassword) throws DBException {
        try (var preparedStatement = connection.prepareStatement(SQL_INSERT)) {
            int i = 1;
            preparedStatement.setLong(i++, userPassword.getUserId());
            preparedStatement.setString(i++, userPassword.getHash());
            preparedStatement.setString(i++, userPassword.getSalt());
            preparedStatement.setString(i, userPassword.getHashMethod().name());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logger.error("Failed to insert user password!", ex);
            throw new DBException(ex);
        }
    }

    private final static String SQL_FIND_BY_USER_ID =
            "SELECT " +
                    "user_id AS user_id, " +
                    "hash AS hash, " +
                    "salt AS salt, " +
                    "hash_method AS hash_method " +
            "FROM user_passwords " +
                    "WHERE user_id = ?";

    @Override
    public @NotNull Optional<UserPassword> findByKey(@NotNull Long userId) throws DBException {
        return findByKey(SQL_FIND_BY_USER_ID, userId);
    }

    @Override
    protected @NotNull UserPassword fetchOne(@NotNull ResultSet resultSet) throws DBException {
        try {
            final long userId = resultSet.getLong("user_id");
            final String hash = resultSet.getString("hash");
            final String salt = resultSet.getString("salt");
            final String hashMethod = resultSet.getString("hash_method");
            return newUserPasswordInstance(userId, hash, salt, PasswordHashing.HashMethod.valueOf(hashMethod));
        } catch (SQLException ex) {
            logger.error("Failed to fetch user password data!", ex);
            throw new DBException(ex);
        }
    }

    private @NotNull UserPassword newUserPasswordInstance(long userId,
                                                          @NotNull String hash,
                                                          @NotNull String salt,
                                                          @NotNull PasswordHashing.HashMethod hashMethod) {
        return entityFactory.newUserPassword(userId, hash, salt, hashMethod);
    }
}


