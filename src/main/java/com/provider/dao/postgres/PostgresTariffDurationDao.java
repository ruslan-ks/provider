package com.provider.dao.postgres;

import com.provider.dao.TariffDurationDao;
import com.provider.dao.exception.DBException;
import com.provider.entity.product.TariffDuration;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class PostgresTariffDurationDao extends TariffDurationDao {

    private static final String SQL_FIND_BY_ID =
            "SELECT " +
                    "tariff_id AS tariff_id, " +
                    "months AS tariff_duration_months, " +
                    "minutes AS tariff_duration_minutes " +
            "FROM tariff_durations WHERE id = ?";

    @Override
    public @NotNull Optional<TariffDuration> findByKey(@NotNull Integer key) throws DBException {
        return findByKey(SQL_FIND_BY_ID, key);
    }

    private static final String SQL_INSERT = "INSERT INTO tariff_durations(tariff_id, months, minutes) " +
            "VALUES (?, ?, ?)";

    @Override
    public boolean insert(@NotNull TariffDuration tariffDuration) throws DBException {
        try (var preparedStatement = connection.prepareStatement(SQL_INSERT)) {
            int i = 1;
            preparedStatement.setInt(i++, tariffDuration.getTariffId());
            preparedStatement.setInt(i++, tariffDuration.getMonths());
            preparedStatement.setLong(i, tariffDuration.getMinutes());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    @Override
    public @NotNull TariffDuration fetchOne(@NotNull ResultSet resultSet) throws DBException {
        try {
            final int tariffId = resultSet.getInt("tariff_id");
            final int months = resultSet.getInt("tariff_duration_months");
            final long minutes = resultSet.getInt("tariff_duration_minutes");
            return entityFactory.newTariffDuration(tariffId, months, minutes);
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }
}
