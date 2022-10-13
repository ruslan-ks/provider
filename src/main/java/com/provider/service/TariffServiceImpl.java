package com.provider.service;

import com.provider.dao.ServiceDao;
import com.provider.dao.exception.DBException;
import com.provider.entity.product.Service;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;

public class TariffServiceImpl extends AbstractService implements TariffService {
    protected TariffServiceImpl() throws DBException {}

    @Override
    public @NotNull List<Service> findAllServices(@NotNull String locale) throws DBException {
        final ServiceDao serviceDao = daoFactory.newServiceDao();
        try (var connection = connectionSupplier.get()) {
            serviceDao.setConnection(connection);
            return serviceDao.findAll(locale);
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }
}
