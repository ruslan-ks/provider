package com.provider.dao;

import com.provider.dao.exception.DBException;
import com.provider.entity.EntityFactory;
import com.provider.entity.SimpleEntityFactory;
import com.provider.entity.product.Service;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public abstract class ServiceDaoTestBase extends AbstractDaoTest {
    private static final EntityFactory entityFactory = SimpleEntityFactory.newInstance();

    /**
     * Template method that returns ServiceDao implementation
     * @return ServiceDao implementation instance
     */
    protected abstract ServiceDao getServiceDao();

    @ParameterizedTest
    @MethodSource("com.provider.TestData#getServiceStream")
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
    @MethodSource("com.provider.TestData#getServiceNamesUkTranslations")
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
    @MethodSource("com.provider.TestData#getServiceStream")
    public void testFindNotExistingTranslation(Service service) throws DBException {
        final ServiceDao serviceDao = getServiceDao();
        serviceDao.setConnection(getConnection());

        serviceDao.insert(service);

        Optional<Service> found = serviceDao.findByKey(service.getId(), "not a language");
        assertTrue(found.isPresent());
        assertEquals(service, found.get());
    }
}
