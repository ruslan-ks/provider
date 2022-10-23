package com.provider.dao.postgres;

import com.provider.dao.TariffDao;
import com.provider.dao.exception.DBException;
import com.provider.dao.postgres.util.PostgresQueryBuilder;
import com.provider.entity.dto.SimpleTariffDto;
import com.provider.entity.dto.TariffDto;
import com.provider.entity.product.Service;
import com.provider.entity.product.Tariff;
import com.provider.entity.product.TariffDuration;
import com.provider.sorting.TariffOrderByField;
import com.provider.sorting.TariffOrderRule;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class PostgresTariffDao extends TariffDao {
    PostgresTariffDao() {}

    private static final String SQL_FIND_BY_ID =
            "SELECT " +
                    "id AS tariff_id, " +
                    "title AS tariff_title, " +
                    "description AS tariff_description, " +
                    "status AS tariff_status, " +
                    "usd_price AS tariff_usd_price, " +
                    "image_file_name AS tariff_image_file_name " +
            "FROM tariffs " +
                    "WHERE id = ?";

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
                    "t.image_file_name AS tariff_image_file_name, " +
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

    private static final List<String> TARIFF_AND_DURATION_FIELDS = List.of(
            "t.id AS tariff_id",
            "COALESCE(tt.title, t.title) AS tariff_title",
            "COALESCE(tt.description, t.description) AS tariff_description",
            "t.status AS tariff_status",
            "t.usd_price AS tariff_usd_price",
            "t.image_file_name AS tariff_image_file_name",
            "td.months AS tariff_duration_months",
            "td.minutes AS tariff_duration_minutes"
    );

    @Override
    public @NotNull List<TariffDto> findFullInfoPage(long offset, int limit, @NotNull String locale, boolean activeOnly,
            @NotNull TariffOrderRule @NotNull... orderRules) throws DBException {
        final List<String> orderByFields = Arrays.stream(orderRules)
                .map(PostgresTariffDao::orderRuleToQueryField)
                .toList();
        final String whereCondition = activeOnly
                ? "t.status = '" + Tariff.Status.ACTIVE.name() + "'"
                : "true";
        final PostgresQueryBuilder queryBuilder = PostgresQueryBuilder.of("tariffs t")
                .addSelect(TARIFF_AND_DURATION_FIELDS)
                .addLeftJoin("tariff_durations td", "td.tariff_id = t.id")
                .addLeftJoin("tariff_translations tt", "tt.tariff_id = t.id AND tt.locale = ?")
                .setWhere(whereCondition)
                .addOrderBy(orderByFields)
                .setOffsetArg(true)
                .setLimitArg(true);
        final String query = queryBuilder.getQuery();
        try (var preparedStatement = connection.prepareStatement(query)) {
            int i = 1;
            preparedStatement.setString(i++, locale);
            preparedStatement.setLong(i++, offset);
            preparedStatement.setInt(i, limit);
            final ResultSet resultSet = preparedStatement.executeQuery();

            final var tariffDurationDao = new PostgresTariffDurationDao();
            final List<TariffDto> tariffDtoList = new ArrayList<>();
            while (resultSet.next()) {
                final Tariff tariff = fetchOne(resultSet);
                final TariffDuration tariffDuration = tariffDurationDao.fetchOne(resultSet);
                final List<Service> tariffServices = findTariffServices(tariff.getId(), locale);
                tariffDtoList.add(SimpleTariffDto.of(tariff, tariffDuration, tariffServices));
            }
            return tariffDtoList;
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    private static final String SQL_INSERT =
            "INSERT INTO tariffs(title, description, status, usd_price, image_file_name) " + "VALUES (?, ?, ?, ?, ?)";

    @Override
    public boolean insert(@NotNull Tariff tariff) throws DBException {
        try (var preparedStatement = connection.prepareStatement(SQL_INSERT,
                Statement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            preparedStatement.setString(i++, tariff.getTitle());
            preparedStatement.setString(i++, tariff.getDescription());
            preparedStatement.setString(i++, tariff.getStatus().name());
            preparedStatement.setBigDecimal(i++, tariff.getUsdPrice());
            preparedStatement.setString(i, tariff.getImageFileName());

            final int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                final ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    tariff.setId(generatedKeys.getInt(1));
                    return true;
                }
                throw new DBException("Failed to obtain generated keys after inserting a row " + tariff);
            }
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
        return false;
    }

    private static final String SQL_ADD_SERVICE = "INSERT INTO tariff_services(tariff_id, service_id) VALUES (?, ?)";

    @Override
    public boolean addServices(int tariffId, @NotNull Set<Integer> serviceIds) throws DBException {
        if (tariffId <= 0) {
            throw new IllegalArgumentException("tariffId <= 0: tariffId = " + tariffId);
        }
        try (var preparedStatement = connection.prepareStatement(SQL_ADD_SERVICE)) {
            for (var serviceId : serviceIds) {
                int i = 1;
                preparedStatement.setInt(i++, tariffId);
                preparedStatement.setInt(i, serviceId);
                preparedStatement.addBatch();
            }
            int[] updatedRows = preparedStatement.executeBatch();
            return Arrays.stream(updatedRows).sum() == serviceIds.size();
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    private static final String SQL_FIND_TARIFF_SERVICES =
            "SELECT " +
                    "s.id AS service_id, " +
                    "COALESCE(st.name, s.name) AS service_name, " +
                    "COALESCE(st.description, s.description) AS service_description " +
            "FROM tariff_services ts " +
            "LEFT JOIN services s " +
                    "ON s.id = ts.service_id " +
            "LEFT JOIN service_translations st " +
                    "ON st.service_id = s.id AND st.locale = ? " +
            "WHERE ts.tariff_id = ? " +
            "ORDER BY s.id";

    @Override
    public List<Service> findTariffServices(int tariffId, @NotNull String locale) throws DBException {
        if (tariffId <= 0) {
            throw new IllegalArgumentException();
        }
        try (var preparedStatement = connection.prepareStatement(SQL_FIND_TARIFF_SERVICES)) {
            int i = 1;
            preparedStatement.setString(i++, locale);
            preparedStatement.setInt(i, tariffId);
            final ResultSet resultSet = preparedStatement.executeQuery();
            final PostgresServiceDao serviceDao = new PostgresServiceDao();
            return serviceDao.fetchAll(resultSet);
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    private static final String SQL_COUNT_ALL = "SELECT COUNT(*) FROM tariffs";

    @Override
    public int countAll() throws DBException {
        return count(SQL_COUNT_ALL);
    }

    private static final String SQL_COUNT_ACTIVE = "SELECT COUNT(*) FROM tariffs t " +
            "WHERE t.status = '" + Tariff.Status.ACTIVE.name() + "'";

    @Override
    public int countActive() throws DBException {
        return count(SQL_COUNT_ACTIVE);
    }

    private int count(@NotNull String countQuery) throws DBException {
        try (var statement = connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery(countQuery);
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            throw new DBException("Failed to get tariffs count from db. Query: " + SQL_COUNT_ALL);
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    private static @NotNull String orderRuleToQueryField(@NotNull TariffOrderRule orderRule) {
        final Map<TariffOrderByField, String> orderByFieldStringMap = Map.of(
                TariffOrderByField.ID, "tariff_id",
                TariffOrderByField.TITLE, "tariff_title",
                TariffOrderByField.STATUS, "tariff_status",
                TariffOrderByField.USD_PRICE, "tariff_usd_price"
        );
        return orderByFieldStringMap.get(orderRule.getOrderByField()) + (orderRule.isDesc() ? " DESC" : "");
    }

    @Override
    protected @NotNull Tariff fetchOne(@NotNull ResultSet resultSet) throws DBException {
        try {
            final int id = resultSet.getInt("tariff_id");
            final String title = resultSet.getString("tariff_title");
            final String description = resultSet.getString("tariff_description");
            final Tariff.Status status = Tariff.Status.valueOf(resultSet.getString("tariff_status"));
            final BigDecimal usdPrice = resultSet.getBigDecimal("tariff_usd_price");
            final String imageFileName = resultSet.getString("tariff_image_file_name");
            return entityFactory.newTariff(id, title, description, status, usdPrice, imageFileName);
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    private @NotNull List<Tariff> fetchAll(@NotNull ResultSet resultSet) throws SQLException, DBException {
        final List<Tariff> tariffList = new ArrayList<>();
        while (resultSet.next()) {
            tariffList.add(fetchOne(resultSet));
        }
        return tariffList;
    }
}
