package com.provider.dao.postgres;

import com.provider.dao.ServiceDao;
import com.provider.dao.exception.DBException;
import com.provider.entity.product.Service;
import com.provider.entity.product.Tariff;
import com.provider.util.Checks;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

public class PostgresServiceDao extends ServiceDao {
    private static final Logger logger = LoggerFactory.getLogger(PostgresServiceDao.class);

    PostgresServiceDao() {}

    private static final String SQL_FIND_BY_ID =
            "SELECT " +
                    "id AS service_id, " +
                    "name AS service_name, " +
                    "description AS service_description " +
            "FROM services " +
            "WHERE id = ?";

    @Override
    public @NotNull Optional<Service> findByKey(@NotNull Integer key) throws DBException {
        return findByKey(SQL_FIND_BY_ID, key);
    }

    private static final String SQL_FIND_ALL_LOCALIZED =
            "SELECT " +
                    "s.id AS service_id, " +
                    "COALESCE(st.name, s.name) AS service_name, " +
                    "COALESCE(st.description, s.description) AS service_description " +
            "FROM services s " +
            "LEFT JOIN service_translations st " +
                    "ON st.service_id = s.id AND st.locale = ? " +
            "ORDER BY s.id";

    @Override
    public @NotNull List<Service> findAll(@NotNull String locale) throws DBException {
        try (var preparedStatement = connection.prepareStatement(SQL_FIND_ALL_LOCALIZED)) {
            preparedStatement.setString(1, locale);
            final ResultSet resultSet = preparedStatement.executeQuery();
            return fetchAll(resultSet);
        } catch (SQLException ex) {
            logger.error("Failed to find all services", ex);
            throw new DBException(ex);
        }
    }

    private static final String SQL_FIND_BY_ID_LOCALIZED =
            "SELECT " +
                    "s.id AS service_id, " +
                    "COALESCE(st.name, s.name) AS service_name, " +
                    "COALESCE(st.description, s.description) AS service_description " +
            "FROM services s " +
            "LEFT JOIN service_translations st " +
                    "ON st.service_id = s.id AND st.locale = ? " +
            "WHERE s.id = ?";

    public @NotNull Optional<Service> findByKey(@NotNull Integer key, @NotNull String locale)
            throws DBException {
        Checks.throwIfInvalidId(key);
        try (var preparedStatement = connection.prepareStatement(SQL_FIND_BY_ID_LOCALIZED)) {
            preparedStatement.setString(1, locale);
            preparedStatement.setInt(2, key);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(fetchOne(resultSet));
            }
        } catch (SQLException ex) {
            logger.error("Failed to find service by key!", ex);
            throw new DBException(ex);
        }
        return Optional.empty();
    }

    private static final String SQL_INSERT = "INSERT INTO services(name, description) VALUES (?, ?)";

    @Override
    public boolean insert(@NotNull Service service) throws DBException {
        try (var preparedStatement = connection.prepareStatement(SQL_INSERT,
                Statement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            preparedStatement.setString(i++, service.getName());
            preparedStatement.setString(i, service.getDescription());
            final int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                final ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    service.setId(generatedKeys.getInt(1));
                    return true;
                }
                throw new DBException("Service was inserted, but no keys were generated");
            }
        } catch (SQLException ex) {
            logger.error("Failed to insert service! Service: {}", service);
            logger.error("Failed to insert service!", ex);
            throw new DBException(ex);
        }
        return false;
    }

    private static final String SQL_INSERT_TRANSLATION =
            "INSERT INTO service_translations(service_id, name, description, locale) VALUES (?, ?, ?, ?)";

    @Override
    public boolean insertTranslation(@NotNull Service service, @NotNull String locale) throws DBException {
        try (var preparedStatement = connection.prepareStatement(SQL_INSERT_TRANSLATION)) {
            int i = 1;
            preparedStatement.setInt(i++, service.getId());
            preparedStatement.setString(i++, service.getName());
            preparedStatement.setString(i++, service.getDescription());
            preparedStatement.setString(i, locale);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logger.error("Failed to insert service translation! Service translation: {}", service);
            logger.error("Failed to insert service translation!", ex);
            throw new DBException(ex);
        }
    }

    @Override
    public @NotNull Map<Service, Integer> findServicesTariffsCount(@NotNull String locale, boolean activeOnly)
            throws DBException {
        try (var preparedStatement = connection.prepareStatement(
                getFindServicesTariffsCountSql(activeOnly))) {
            preparedStatement.setString(1, locale);
            final ResultSet resultSet = preparedStatement.executeQuery();
            final Map<Service, Integer> servicesTariffsCount = new HashMap<>();
            while (resultSet.next()) {
                final Service service = fetchOne(resultSet);
                final int count = resultSet.getInt("service_count");
                servicesTariffsCount.put(service, count);
            }
            return servicesTariffsCount;
        } catch (SQLException ex) {
            logger.error("Failed to obtain services tariffs count", ex);
            throw new DBException(ex);
        }
    }

    private static @NotNull String getFindServicesTariffsCountSql(boolean activeOnly) {
        final String selectAndJoinQueryPart =
                "SELECT " +
                        "s.id AS service_id, " +
                        "coalesce(st.name, s.name) AS service_name, " +
                        "coalesce(st.description, s.description) AS service_description, " +
                        "count(ts.tariff_id) AS service_count " +
                "FROM services s " +
                "INNER JOIN tariff_services ts " +
                        "ON s.id = ts.service_id " +
                "INNER JOIN tariffs t ON t.id = ts.tariff_id " +
                "LEFT JOIN service_translations st " +
                        "ON s.id = st.service_id AND st.locale = ? ";
        final String wherePart = "WHERE t.status = '" + Tariff.Status.ACTIVE + "' ";
        final String groupByPart = "GROUP BY s.id, st.name, st.description";
        if (activeOnly) {
            return selectAndJoinQueryPart + wherePart + groupByPart;
        }
        return selectAndJoinQueryPart + groupByPart;
    }

    private static final String SQL_COUNT_DISTINCT_TARIFFS_INCLUDING_SERVICES =
            "SELECT " +
                    "count(DISTINCT ts.tariff_id) " +
            "FROM tariff_services ts " +
            "INNER JOIN tariffs t " +
                    "ON t.id = ts.tariff_id ";

    @Override
    public int countDistinctTariffsIncludingServices(@NotNull Set<Integer> serviceIds, boolean activeOnly)
            throws DBException {
        if (serviceIds.isEmpty()) {
            throw new IllegalArgumentException("Service id set is empty!");
        }
        serviceIds.forEach(Checks::throwIfInvalidId);

        final String statusCondition = activeOnly ? "t.status = '" + Tariff.Status.ACTIVE.name() + "' AND " : "";
        final String servicesCondition = serviceIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", ", "ts.service_id IN (", ")"));
        final String query = SQL_COUNT_DISTINCT_TARIFFS_INCLUDING_SERVICES + "WHERE " + statusCondition +
                servicesCondition;
        try (var statement = connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            throw new RuntimeException("Failed to obtain any result set rows");
        } catch (SQLException ex) {
            logger.error("Failed to count distinct tariffs that include services! service ids: {}", serviceIds);
            logger.error("Failed to count distinct tariffs that include some services!", ex);
            throw new DBException(ex);
        }
    }

    @Override
    public @NotNull Service fetchOne(@NotNull ResultSet resultSet) throws DBException {
        try {
            final int id = resultSet.getInt("service_id");
            final String name = resultSet.getString("service_name");
            final String description = resultSet.getString("service_description");
            return entityFactory.newService(id, name, description);
        } catch (SQLException ex) {
            logger.error("Failed to fetch one service! ResultSet: {}", resultSet);
            logger.error("Failed to fetch one service!", ex);
            throw new DBException(ex);
        }
    }

    @NotNull List<Service> fetchAll(@NotNull ResultSet resultSet) throws DBException {
        final List<Service> serviceList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                serviceList.add(fetchOne(resultSet));
            }
        } catch (SQLException ex) {
            logger.error("Failed to fetch all services! ResultSet: {}", resultSet);
            logger.error("Failed to fetch all services!", ex);
            throw new DBException(ex);
        }
        return serviceList;
    }
}
