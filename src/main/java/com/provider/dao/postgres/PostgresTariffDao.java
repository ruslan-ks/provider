package com.provider.dao.postgres;

import com.provider.dao.TariffDao;
import com.provider.dao.exception.DBException;
import com.provider.entity.dto.SimpleTariffDto;
import com.provider.entity.dto.TariffDto;
import com.provider.entity.product.Service;
import com.provider.entity.product.Tariff;
import com.provider.entity.product.TariffDuration;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class PostgresTariffDao extends TariffDao {
    PostgresTariffDao() {}

    // TODO: add addService(s) method

    private static final String SQL_FIND_BY_ID =
            "SELECT " +
                    "id AS tariff_id, " +
                    "title AS tariff_title, " +
                    "description AS tariff_description, " +
                    "status AS tariff_status, " +
                    "usd_price AS tariff_usd_price " +
            "FROM tariffs WHERE id = ?";

    @Override
    public @NotNull Optional<Tariff> findByKey(@NotNull Integer key) throws DBException {
        return findByKey(SQL_FIND_BY_ID, key);
    }

    private static final String SQL_FIND_FULL_INFO_BY_ID =
            "SELECT " +
                    "t.id AS tariff_id, " +
                    "COALESCE(tt.title, t.title) AS tariff_title, " +
                    "COALESCE(tt.description, t.description) AS tariff_description, " +
                    "t.status AS tariff_status, " +
                    "t.usd_price AS tariff_usd_price, " +
                    "td.months AS tariff_duration_months, " +
                    "td.minutes AS tariff_duration_minutes, " +
                    "s.id AS service_id, " +
                    "COALESCE(st.name, s.name) AS service_name, " +
                    "COALESCE(st.description, s.description) AS service_description " +
            "FROM tariffs t " +
            "LEFT JOIN tariff_durations td " +
                    "ON td.tariff_id = t.id " +
            "LEFT JOIN tariff_services ts " +
                    "ON ts.tariff_id = t.id " +
            "LEFT JOIN services s " +
                    "ON s.id = ts.service_id " +
            "LEFT JOIN tariff_translations tt " +
                    "ON tt.tariff_id = t.id AND tt.locale = ? " +
            "LEFT JOIN service_translations st " +
                    "ON st.service_id = s.id AND st.locale = ? " +
            "WHERE t.id = ?";

    @Override
    public @NotNull Optional<TariffDto> findFullInfoByKey(int id, @NotNull String locale) throws DBException {
        try (var preparedStatement = connection.prepareStatement(SQL_FIND_FULL_INFO_BY_ID)) {
            int i = 1;
            preparedStatement.setString(i++, locale);
            preparedStatement.setString(i++, locale);
            preparedStatement.setInt(i, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                final Tariff tariff = fetchOne(resultSet);

                final PostgresTariffDurationDao tariffDurationDao = new PostgresTariffDurationDao();
                final TariffDuration tariffDuration = tariffDurationDao.fetchOne(resultSet);

                final PostgresServiceDao serviceDao = new PostgresServiceDao();
                final List<Service> serviceList = new ArrayList<>();
                serviceList.add(serviceDao.fetchOne(resultSet));
                while (resultSet.next()) {
                    serviceList.add(serviceDao.fetchOne(resultSet));
                }
                return Optional.of(SimpleTariffDto.of(tariff, tariffDuration, serviceList));
            }
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
        return Optional.empty();
    }

    private static final String SQL_INSERT = "INSERT INTO tariffs(title, description, status, usd_price) " +
            "VALUES (?, ?, ?, ?)";

    @Override
    public boolean insert(@NotNull Tariff tariff) throws DBException {
        try (var preparedStatement = connection.prepareStatement(SQL_INSERT,
                Statement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            preparedStatement.setString(i++, tariff.getTitle());
            preparedStatement.setString(i++, tariff.getDescription());
            preparedStatement.setString(i++, tariff.getStatus().name());
            preparedStatement.setBigDecimal(i, tariff.getUsdPrice());

            final int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                final ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    tariff.setId(generatedKeys.getInt(1));
                    return true;
                }
                throw new DBException("Couldn't obtain generated keys after inserting a row " + tariff);
            }
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
        return false;
    }

    private static final String SQL_ADD_SERVICE = "INSERT INTO tariff_services(tariff_id, service_id) VALUES (?, ?)";

    public boolean addServices(@NotNull Tariff tariff, @NotNull Set<Service> services) throws DBException {
        try (var preparedStatement = connection.prepareStatement(SQL_ADD_SERVICE)) {
            for (var service : services) {
                int i = 1;
                preparedStatement.setInt(i++, tariff.getId());
                preparedStatement.setInt(i, service.getId());
                preparedStatement.addBatch();
            }
            int[] updatedRows = preparedStatement.executeBatch();
            return Arrays.stream(updatedRows).sum() == services.size();
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    private static @NotNull String orderRuleToQuery(@NotNull OrderRule orderRule) {
        final String fieldName = switch (orderRule.getOrderByField()) {
            case ID -> "tariff_id";
            case TITLE -> "tariff_title";
            case STATUS -> "tariff_status";
            case USD_PRICE -> "tariff_usd_price";
        };
        return fieldName + (orderRule.isDesc() ? " DESC" : "");
    }

    @Override
    protected @NotNull Tariff fetchOne(@NotNull ResultSet resultSet) throws DBException {
        try {
            final int id = resultSet.getInt("tariff_id");
            final String title = resultSet.getString("tariff_title");
            final String description = resultSet.getString("tariff_description");
            final Tariff.Status status = Tariff.Status.valueOf(resultSet.getString("tariff_status"));
            final BigDecimal usdPrice = resultSet.getBigDecimal("tariff_usd_price");
            return entityFactory.newTariff(id, title, description, status, usdPrice);
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    private List<Tariff> fetchAll(@NotNull ResultSet resultSet) throws SQLException, DBException {
        final List<Tariff> tariffList = new ArrayList<>();
        while (resultSet.next()) {
            tariffList.add(fetchOne(resultSet));
        }
        return tariffList;
    }
}
