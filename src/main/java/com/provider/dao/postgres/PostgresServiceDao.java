package com.provider.dao.postgres;

import com.provider.dao.ServiceDao;
import com.provider.dao.exception.DBException;
import com.provider.entity.product.Service;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class PostgresServiceDao extends ServiceDao {
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
            throw new DBException(ex);
        }
    }

    private static final String SQL_FIND_BY_ID_LOCALIZED =
            "SELECT " +
                    "s.id AS service_id, " +
                    "COALESCE(st.name, s.name) AS service_name, " +
                    "COALESCE(st.description, s.description) AS service_description " +
            "FROM services s " +
                    "LEFT JOIN service_translations st ON st.service_id = s.id AND st.locale = ? " +
            "WHERE s.id = ?";

    public @NotNull Optional<Service> findByKey(@NotNull Integer key, @NotNull String locale)
            throws DBException {
        try (var preparedStatement = connection.prepareStatement(SQL_FIND_BY_ID_LOCALIZED)) {
            preparedStatement.setString(1, locale);
            preparedStatement.setInt(2, key);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(fetchOne(resultSet));
            }
        } catch (SQLException ex) {
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
            throw new DBException(ex);
        }
    }

    private static final String SQL_FIND_ALL =
            "SELECT " +
                    "id AS service_id, " +
                    "name AS service_name, " +
                    "description AS description " +
            "FROM services";

    @Override
    public @NotNull List<Service> findAll() throws DBException {
        try (var statement = connection.createStatement();
             var resultSet = statement.executeQuery(SQL_FIND_ALL)) {
            return fetchAll(resultSet);
        } catch (SQLException ex) {
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
            throw new DBException(ex);
        }
    }

    private @NotNull List<Service> fetchAll(@NotNull ResultSet resultSet) throws DBException {
        final List<Service> serviceList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                serviceList.add(fetchOne(resultSet));
            }
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
        return serviceList;
    }
}
