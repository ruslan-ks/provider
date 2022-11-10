package com.provider.service.impl;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.provider.dao.ServiceDao;
import com.provider.dao.TariffDao;
import com.provider.dao.TariffDurationDao;
import com.provider.dao.exception.DBException;
import com.provider.dao.transaction.Transaction;
import com.provider.entity.dto.TariffDto;
import com.provider.entity.product.Service;
import com.provider.entity.product.Tariff;
import com.provider.entity.product.TariffDuration;
import com.provider.service.TariffService;
import com.provider.service.exception.ValidationException;
import com.provider.sorting.TariffOrderRule;
import com.provider.validation.ServiceValidator;
import com.provider.validation.TariffValidator;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.sql.SQLException;
import java.util.*;

public class TariffServiceImpl extends TariffService {
    private static final Logger logger = LoggerFactory.getLogger(TariffServiceImpl.class);

    protected TariffServiceImpl() throws DBException {}

    @Override
    public @NotNull List<Service> findAllServices(@NotNull String locale) throws DBException {
        final ServiceDao serviceDao = daoFactory.newServiceDao();
        try (var connection = connectionSupplier.get()) {
            serviceDao.setConnection(connection);
            return serviceDao.findAll(locale);
        } catch (SQLException ex) {
            logFailedToCloseConnection(logger, ex);
            throw new DBException(ex);
        }
    }

    @Override
    public boolean insertService(@NotNull Service service) throws DBException, ValidationException {
        final ServiceValidator serviceValidator = validatorFactory.getServiceValidator();
        if (!serviceValidator.isValidName(service.getName())
                || !serviceValidator.isValidDescription(service.getDescription())) {
            throw new ValidationException();
        }
        final ServiceDao serviceDao = daoFactory.newServiceDao();
        try (var connection = connectionSupplier.get()) {
            serviceDao.setConnection(connection);
            return serviceDao.insert(service);
        } catch (SQLException ex) {
            logFailedToCloseConnection(logger, ex);
            throw new DBException(ex);
        }
    }

    @Override
    public @NotNull List<TariffDto> findTariffsPage(long offset,
                                                    int limit,
                                                    @NotNull String locale,
                                                    @NotNull Set<TariffOrderRule> orderRules,
                                                    @NotNull Set<Integer> serviceIds,
                                                    boolean activeOnly) throws DBException {
        final TariffDao tariffDao = daoFactory.newTariffDao();
        try (var connection = connectionSupplier.get()) {
            tariffDao.setConnection(connection);
            return tariffDao.findFullInfoPage(offset, limit, locale, orderRules, serviceIds, activeOnly);
        } catch (SQLException ex) {
            logFailedToCloseConnection(logger, ex);
            throw new DBException(ex);
        }
    }

    @Override
    public @NotNull Optional<Tariff> findTariffById(int tariffId) throws DBException {
        final TariffDao tariffDao = daoFactory.newTariffDao();
        try (var connection = connectionSupplier.get()) {
            tariffDao.setConnection(connection);
            return tariffDao.findByKey(tariffId);
        } catch (SQLException ex) {
            logFailedToCloseConnection(logger, ex);
            throw new DBException(ex);
        }
    }

    @Override
    public @NotNull Optional<TariffDto> findTariffFullInfoById(int tariffId, @NotNull String locale)
            throws DBException {
        final TariffDao tariffDao = daoFactory.newTariffDao();
        try (var connection = connectionSupplier.get()) {
            tariffDao.setConnection(connection);
            return tariffDao.findFullInfoByKey(tariffId, locale);
        } catch (SQLException ex) {
            logFailedToCloseConnection(logger, ex);
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
            logFailedToCloseConnection(logger, ex);
            throw new DBException(ex);
        }
    }

    @Override
    public int countActiveTariffs() throws DBException {
        final TariffDao tariffDao = daoFactory.newTariffDao();
        try (var connection = connectionSupplier.get()) {
            tariffDao.setConnection(connection);
            return tariffDao.countActive();
        } catch (SQLException ex) {
            logFailedToCloseConnection(logger, ex);
            throw new DBException(ex);
        }
    }

    @Override
    public boolean insertTariff(@NotNull Tariff tariff, @NotNull TariffDuration tariffDuration,
                                @NotNull Set<Integer> serviceIds) throws ValidationException, DBException {
        throwIfInvalid(tariff, tariffDuration);
        if (serviceIds.isEmpty()) {
            throw new IllegalArgumentException("Service id set " + serviceIds + " is empty");
        }
        final TariffDao tariffDao = daoFactory.newTariffDao();
        final TariffDurationDao tariffDurationDao = daoFactory.newTariffDurationDao();
        final ServiceDao serviceDao = daoFactory.newServiceDao();
        try (var transaction = Transaction.of(connectionSupplier.get(), tariffDao, tariffDurationDao,
                serviceDao)) {
            for (var serviceId : serviceIds) {
                if (serviceDao.findByKey(serviceId).isEmpty()) {
                    return false;
                }
            }
            try {
                final boolean tariffInserted = tariffDao.insert(tariff);
                tariffDuration.setTariffId(tariff.getId());
                final boolean tariffDurationInserted = tariffDurationDao.insert(tariffDuration);
                final boolean servicesAdded = tariffDao.addServices(tariff.getId(), serviceIds);
                if (tariffInserted && tariffDurationInserted && servicesAdded) {
                    transaction.commit();
                    return true;
                }
            } catch (Throwable ex) {
                transaction.rollback();
                logger.error("Tariff insertion transaction failed", ex);
                logger.error("Tariff insertion transaction failed! Tariff: {}, tariffDuration: {}, serviceIds: {}",
                        tariff, tariffDuration, serviceIds);
                throw ex;
            }
            transaction.rollback();
        }
        return false;
    }

    private void throwIfInvalid(@NotNull Tariff tariff, @NotNull TariffDuration tariffDuration)
            throws ValidationException {
        final TariffValidator validator = validatorFactory.getTariffValidator();
        if (!validator.isValidTitle(tariff.getTitle())
                || !validator.isValidDescription(tariff.getDescription())
                || !validator.isValidUsdPrice(tariff.getUsdPrice())
                || !validator.isValidDuration(tariffDuration.getMonths(), tariffDuration.getMinutes())
                || !validator.isValidImageFileName(tariff.getImageFileName())) {
            throw new ValidationException("Invalid Tariff or TariffDuration property values");
        }
    }

    @Override
    public @NotNull Map<Service, Integer> findAllServicesTariffsCount(@NotNull String locale, boolean activeOnly)
            throws DBException {
        final ServiceDao serviceDao = daoFactory.newServiceDao();
        try (var connection = connectionSupplier.get()) {
            serviceDao.setConnection(connection);
            return serviceDao.findServicesTariffsCount(locale, activeOnly);
        } catch (SQLException ex) {
            logFailedToCloseConnection(logger, ex);
            throw new DBException(ex);
        }
    }

    @Override
    public int countDistinctTariffsIncludingServices(@NotNull Set<Integer> serviceIds, boolean activeOnly) throws DBException {
        final ServiceDao serviceDao = daoFactory.newServiceDao();
        try (var connection = connectionSupplier.get()) {
            serviceDao.setConnection(connection);
            return serviceDao.countDistinctTariffsIncludingServices(serviceIds, activeOnly);
        } catch (SQLException ex) {
            logFailedToCloseConnection(logger, ex);
            throw new DBException(ex);
        }
    }

    @Override
    public boolean updateTariff(@NotNull final Tariff tariff) throws DBException, ValidationException {
        throwIfInvalid(tariff.getTitle(), tariff.getDescription(), tariff.getImageFileName());
        final TariffDao tariffDao = daoFactory.newTariffDao();
        try (var connection = connectionSupplier.get()) {
            tariffDao.setConnection(connection);
            tariffDao.findByKey(tariff.getId())
                    .orElseThrow(NoSuchElementException::new);
            return tariffDao.update(tariff);
        } catch (SQLException ex) {
            logFailedToCloseConnection(logger, ex);
            throw new DBException(ex);
        }
    }

    @Override
    public boolean upsertTariffTranslation(@NotNull final Tariff tariff, @NotNull String locale)
            throws DBException, ValidationException {
        throwIfInvalid(tariff.getTitle(), tariff.getDescription(), tariff.getImageFileName());
        final TariffDao tariffDao = daoFactory.newTariffDao();
        try (var transaction = Transaction.of(connectionSupplier.get(), tariffDao)) {
            try {
                final Tariff foundTariff = tariffDao.findByKey(tariff.getId())
                        .orElseThrow(NoSuchElementException::new);
                foundTariff.setStatus(tariff.getStatus());
                foundTariff.setImageFileName(tariff.getImageFileName());
                final boolean tariffUpdated = tariffDao.update(foundTariff);
                final boolean translationUpserted = tariffDao.upsertTranslation(tariff, locale);
                if (tariffUpdated && translationUpserted) {
                    transaction.commit();
                    return true;
                }
            } catch (Throwable ex) {
                logger.error("Failed to execute transaction", ex);
                logger.error("Failed to upsert tariff translation: {}", tariff);
                transaction.rollback();
                throw ex;
            }
            transaction.rollback();
        }
        return false;
    }

    private void throwIfInvalid(@NotNull String title, @NotNull String description,
                                        @NotNull String imageFileName) throws ValidationException {
        final TariffValidator validator = validatorFactory.getTariffValidator();
        if (!validator.isValidTitle(title)
                || !validator.isValidDescription(description)
                || !validator.isValidImageFileName(imageFileName)) {
            throw new ValidationException("Invalid Tariff property values");
        }
    }

    @Override
    public void writeTariffPdf(@NotNull TariffDto tariffDto, @NotNull OutputStream os) {
        final Tariff tariff = tariffDto.getTariff();
        final PdfWriter pdfWriter = new PdfWriter(os);
        final PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.addNewPage();

        try (var document = new Document(pdfDocument)) {
            final var titleParagraph = new Paragraph(tariff.getTitle());
            document.add(titleParagraph);

            final var priceParagraph = new Paragraph("Price: $" + tariff.getUsdPrice());
            document.add(priceParagraph);

            final TariffDuration duration = tariffDto.getDuration();
            final var durationParagraph = new Paragraph("Duration: " + duration.getMonths()
                    + " months, " + duration.getMinutes() + " minutes");
            document.add(durationParagraph);

            final var descriptionParagraph = new Paragraph(tariff.getDescription());
            document.add(descriptionParagraph);

            final var list = new com.itextpdf.layout.element.List();
            for (var service : tariffDto.getServices()) {
                list.add(service.getName() + ": " + service.getDescription());
            }
            document.add(list);
        }
    }
}
