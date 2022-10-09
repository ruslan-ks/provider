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

    private static final String SQL_FIND_BY_ID = "SELECT id AS id, name AS name FROM services WHERE id = ?";

    @Override
    public @NotNull Optional<Service> findByKey(@NotNull Integer key) throws DBException {
        try (var preparedStatement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            preparedStatement.setInt(1, key);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(fetchService(resultSet));
            }
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
        return Optional.empty();
    }

    private static final String SQL_INSERT = "INSERT INTO services(name) VALUES (?)";

    @Override
    public boolean insert(@NotNull Service service) throws DBException {
        try (var preparedStatement = connection.prepareStatement(SQL_INSERT,
                Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, service.getName());
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

    private static final String SQL_SELECT_ALL = "SELECT id AS id, name AS name FROM services";

    @Override
    public @NotNull List<Service> findAll() throws DBException {
        try (var statement = connection.createStatement();
             var resultSet = statement.executeQuery(SQL_SELECT_ALL)) {
            return fetchAllServices(resultSet);
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    private @NotNull List<Service> fetchAllServices(@NotNull ResultSet resultSet) throws SQLException {
        final List<Service> serviceList = new ArrayList<>();
        while (resultSet.next()) {
            serviceList.add(fetchService(resultSet));
        }
        return serviceList;
    }

    private @NotNull Service fetchService(@NotNull ResultSet resultSet) throws SQLException {
        final int id = resultSet.getInt("id");
        final String name = resultSet.getString("name");
        return entityFactory.newService(id, name);
    }
}
