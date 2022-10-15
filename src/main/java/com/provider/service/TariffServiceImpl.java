package com.provider.service;

import com.provider.dao.ServiceDao;
import com.provider.dao.TariffDao;
import com.provider.dao.exception.DBException;
import com.provider.entity.dto.TariffDto;
import com.provider.entity.product.Service;
import com.provider.service.exception.InvalidPropertyException;
import com.provider.validation.ServiceValidator;
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

    @Override
    public boolean insertService(@NotNull Service service) throws DBException, InvalidPropertyException {
        final ServiceValidator serviceValidator = validatorFactory.getServiceValidator();
        if (!serviceValidator.isValidName(service.getName())
                || !serviceValidator.isValidDescription(service.getDescription())) {
            throw new InvalidPropertyException();
        }
        final ServiceDao serviceDao = daoFactory.newServiceDao();
        try (var connection = connectionSupplier.get()) {
            serviceDao.setConnection(connection);
            return serviceDao.insert(service);
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    @Override
    public @NotNull List<TariffDto> findTariffsPage(long offset, int limit, @NotNull String locale) throws DBException {
        final TariffDao tariffDao = daoFactory.newTariffDao();
        try (var connection = connectionSupplier.get()) {
            tariffDao.setConnection(connection);
            return tariffDao.findFullInfoPage(offset, limit, locale);
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    @Override
    public int countAllTariffs() throws DBException {
        final TariffDao tariffDao = daoFactory.newTariffDao();
        try (var connection = connectionSupplier.get()) {
            tariffDao.setConnection(connection);
            return tariffDao.countAll();
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }
}
