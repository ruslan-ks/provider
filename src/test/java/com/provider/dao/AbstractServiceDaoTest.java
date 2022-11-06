package com.provider.dao;

import com.provider.TestData;
import com.provider.dao.exception.DBException;
import com.provider.entity.product.Service;
import com.provider.entity.product.Tariff;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractServiceDaoTest extends AbstractDaoTest {
    /**
     * Returns ServiceDao implementation instance
     * @return ServiceDao implementation instance
     */
    protected abstract ServiceDao getServiceDao();

    /**
     * Returns TariffDao implementation instance
     * @return TariffDao implementation instance
     */
    protected abstract TariffDao getTariffDao();

    @ParameterizedTest
    @MethodSource("com.provider.TestData#serviceStream")
    public void testInsertAndFindByKey(Service service) throws DBException {
        final ServiceDao serviceDao = getServiceDao();
        serviceDao.setConnection(getConnection());

        final boolean inserted = serviceDao.insert(service);
        assertTrue(inserted);
        assertNotEquals(0, service.getId());

        final Optional<Service> foundService = serviceDao.findByKey(service.getId());
        assertTrue(foundService.isPresent());
        assertEquals(service, foundService.get());
    }

    @ParameterizedTest
    @MethodSource("com.provider.TestData#serviceNamesUkTranslations")
    public void testInsertAndFindTranslation(String serviceName, String translatedServiceName, String lang)
            throws DBException {
        final ServiceDao serviceDao = getServiceDao();
        serviceDao.setConnection(getConnection());

        final Service service = entityFactory.newService(0, serviceName, serviceName + " description");
        serviceDao.insert(service);
        final Service serviceTranslation = entityFactory.newService(service.getId(), translatedServiceName,
                translatedServiceName + " переклад");

        final boolean inserted = serviceDao.insertTranslation(serviceTranslation, lang);
        assertTrue(inserted);

        final Optional<Service> foundTranslation = serviceDao.findByKey(service.getId(), lang);
        assertTrue(foundTranslation.isPresent());

        assertEquals(serviceTranslation, foundTranslation.get());
    }

    @ParameterizedTest
    @MethodSource("com.provider.TestData#serviceStream")
    public void testFindNotExistingTranslation(Service service) throws DBException {
        final ServiceDao serviceDao = getServiceDao();
        serviceDao.setConnection(getConnection());

        serviceDao.insert(service);

        Optional<Service> found = serviceDao.findByKey(service.getId(), "not a language");
        assertTrue(found.isPresent());
        assertEquals(service, found.get());
    }

    /**
     * Adding services without locale and obtaining them providing a non-existing locale, so
     * default service content must be obtained
     */
    @Test
    public void testFindAllServicesWithUnknownLocale() throws DBException {
        final ServiceDao serviceDao = getServiceDao();
        serviceDao.setConnection(getConnection());

        final List<Service> services = TestData.serviceStream().toList();
        for (var service : services) {
            serviceDao.insert(service);
        }

        final List<Service> obtainedServices = serviceDao.findAll("notALocale");
        assertTrue(obtainedServices.containsAll(services));
    }

    @Test
    public void testFindAllServicesUkTranslations() throws DBException {
        final ServiceDao serviceDao = getServiceDao();
        serviceDao.setConnection(getConnection());

        final List<Service> services = TestData.serviceStream(10)
                .toList();

        final List<Service> serviceTranslations = new ArrayList<>();
        final String locale = "uk";

        int i = 0;
        for (var service : services) {
            serviceDao.insert(service);
            final Service serviceTranslation = entityFactory.newService(service.getId(), "Сервіс " + i,
                    "Опис сервісу " + i);
            serviceTranslations.add(serviceTranslation);
            serviceDao.insertTranslation(serviceTranslation, locale);
            i++;
        }

        final List<Service> obtainedServiceTranslations = serviceDao.findAll(locale);
        assertTrue(obtainedServiceTranslations.containsAll(serviceTranslations));
    }

    /**
     * ServiceDao tests that also require TariffDao functionality
     */
    @Nested
    class TariffsServicesTest {
        @Test
        public void testFindAllServicesTariffsCount() throws DBException {
            final Set<Service> services = TestData.serviceStream(10)
                    .collect(Collectors.toSet());
            final List<Tariff> tariffs = TestData.tariffStream()
                    .toList();

            final TariffDao tariffDao = getTariffDao();
            tariffDao.setConnection(getConnection());
            final ServiceDao serviceDao = getServiceDao();
            serviceDao.setConnection(getConnection());

            for (var service : services) {
                serviceDao.insert(service);
            }
            final Set<Integer> serviceIds = services.stream()
                    .map(Service::getId)
                    .collect(Collectors.toSet());

            final Map<Tariff, Set<Integer>> tariffServiceIdsMap = new HashMap<>();
            for (int i = 0; i < tariffs.size(); i++) {
                final var tariff = tariffs.get(i);
                tariffDao.insert(tariff);

                // tariff 0 - no services
                // tariff 1: 1 service
                // tariff 2: 2 services
                // ...
                final Set<Integer> tariffServiceIds = serviceIds.stream()
                        .limit(i)
                        .collect(Collectors.toSet());
                tariffDao.addServices(tariff.getId(), tariffServiceIds);
                tariffServiceIdsMap.put(tariff, tariffServiceIds);
            }

            // key - service id, value - tariffs with this service count
            final Map<Integer, Long> expectedServiceTariffCount = tariffServiceIdsMap.values().stream()
                    .flatMap(Set::stream)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            final Map<Integer, Long> actualServiceTariffTariffCount =
                    serviceDao.findServicesTariffsCount("notALocale", false)
                            .entrySet()
                            .stream()
                            .collect(Collectors.toMap(e -> e.getKey().getId(), e -> (long) e.getValue()));
            assertEquals(expectedServiceTariffCount, actualServiceTariffTariffCount);
        }

        @Test
        public void testCountActiveDistinctTariffsIncludingServices() throws DBException {
            final Set<Service> services = TestData.serviceStream(10)
                    .collect(Collectors.toSet());
            final List<Tariff> tariffs = TestData.tariffStream()
                    .toList();

            final TariffDao tariffDao = getTariffDao();
            tariffDao.setConnection(getConnection());
            final ServiceDao serviceDao = getServiceDao();
            serviceDao.setConnection(getConnection());

            for (var service : services) {
                serviceDao.insert(service);
            }
            final Set<Integer> serviceIds = services.stream()
                    .map(Service::getId)
                    .collect(Collectors.toSet());

            for (var tariff : tariffs) {
                tariffDao.insert(tariff);
                tariffDao.addServices(tariff.getId(), serviceIds);  // Add all services to all tariffs
            }

            final int expectedServicesCount = tariffs.size();
            final int distinctTariffsIncludingAllServicesCount =
                    serviceDao.countDistinctTariffsIncludingServices(serviceIds, true);
            assertEquals(expectedServicesCount, distinctTariffsIncludingAllServicesCount);
        }
    }
}
