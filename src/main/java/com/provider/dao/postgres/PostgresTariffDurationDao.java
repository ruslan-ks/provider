package com.provider.dao.postgres;

import com.provider.dao.TariffDurationDao;
import com.provider.dao.exception.DBException;
import com.provider.entity.product.TariffDuration;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

// TODO: implement all methods
public class PostgresTariffDurationDao extends TariffDurationDao {
    @Override
    public @NotNull Optional<TariffDuration> findByKey(@NotNull Integer key) throws DBException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean insert(@NotNull TariffDuration entity) throws DBException {
        throw new UnsupportedOperationException();
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
