package com.provider.dao;

import com.provider.TestData;
import com.provider.dao.exception.DBException;
import com.provider.entity.product.Service;
import com.provider.entity.product.Tariff;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractTariffDaoTest extends AbstractDaoTest {
    protected static TariffDao tariffDao;
    protected static ServiceDao serviceDao;

    @ParameterizedTest
    @MethodSource("com.provider.TestData#tariffStream")
    public void testInsert(Tariff tariff) throws DBException {
        final boolean inserted = tariffDao.insert(tariff);
        assertTrue(inserted);
        assertNotEquals(0, tariff.getId());
    }

    @ParameterizedTest
    @MethodSource("com.provider.TestData#tariffStream")
    public void testFindByKey(Tariff tariff) throws DBException {
        tariffDao.insert(tariff);
        final Optional<Tariff> found = tariffDao.findByKey(tariff.getId());
        assertTrue(found.isPresent());
        assertEquals(tariff, found.orElseThrow());
    }

    @ParameterizedTest
    @MethodSource("com.provider.TestData#tariffStream")
    public void testUpdate(Tariff tariff) throws DBException {
        tariffDao.insert(tariff);

        tariff.setTitle("New title " + tariff.getId());
        tariff.setDescription("New description " + tariff.getId());
        tariff.setImageFileName("newNotRealImage.png");
        tariff.setStatus(revertStatus(tariff.getStatus()));
        tariffDao.update(tariff);

        final Optional<Tariff> found = tariffDao.findByKey(tariff.getId());
        assertTrue(found.isPresent());
        assertEquals(tariff, found.orElseThrow());
    }

    private Tariff.Status revertStatus(Tariff.Status status) {
        return status == Tariff.Status.ACTIVE
                ? Tariff.Status.HIDDEN
                : Tariff.Status.ACTIVE;
    }

    @ParameterizedTest
    @MethodSource("com.provider.TestData#tariffStream")
    public void testAddAndFindServices(Tariff tariff) throws DBException {
        tariffDao.insert(tariff);

        final Set<Service> services = TestData.serviceStream(10)
                .collect(Collectors.toSet());
        for (var service : services) {
            serviceDao.insert(service);
        }
        final Set<Integer> serviceIds = services.stream()
                .map(Service::getId)
                .collect(Collectors.toSet());

        tariffDao.addServices(tariff.getId(), serviceIds);

        final var foundTariffServices = tariffDao.findTariffServices(tariff.getId(), "not a locale");
        assertFalse(foundTariffServices.isEmpty());
        assertTrue(foundTariffServices.containsAll(services));
        assertEquals(services.size(), foundTariffServices.size());
    }

    @Test
    public void testCount() throws DBException {
        final List<Tariff> tariffs = TestData.tariffStream(16).toList();
        for (int i = 0; i < tariffs.size(); i++) {
            final Tariff tariff = tariffs.get(i);
            if (i % 2 == 0) {
                tariff.setStatus(revertStatus(tariff.getStatus()));
            }
            tariffDao.insert(tariff);
        }

        final int count = tariffDao.countAll();
        assertEquals(tariffs.size(), count);

        final long expectedActiveCount = tariffs.stream()
                .filter(t -> t.getStatus() == Tariff.Status.ACTIVE)
                .count();
        final int actualActiveCount = tariffDao.countActive();
        assertEquals(expectedActiveCount, actualActiveCount);
    }
}
