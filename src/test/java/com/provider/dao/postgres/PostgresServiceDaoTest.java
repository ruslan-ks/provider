package com.provider.dao.postgres;

import com.provider.dao.ConnectionSupplier;
import com.provider.dao.DaoTestData;
import com.provider.dao.DaoTestUtil;
import com.provider.dao.ServiceDao;
import com.provider.dao.exception.DBException;
import com.provider.entity.product.Service;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class PostgresServiceDaoTest {

    @BeforeEach
    public void prepare() throws DBException, SQLException {
        clearAllServices();
    }

    @AfterEach
    public void cleanUp() throws DBException, SQLException {
        clearAllServices();
    }

    private void clearAllServices() throws DBException, SQLException {
        try (var connection = getConnectionSupplier().get();
             var statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM services WHERE TRUE");
        }
    }

    @ParameterizedTest
    @MethodSource("com.provider.dao.DaoTestData#getServiceStream")
    public void testInsertAndFindByKey(Service service) throws DBException {
        final ServiceDao serviceDao = getServiceDao();
        serviceDao.setConnection(getConnectionSupplier().get());

        final boolean inserted = serviceDao.insert(service);
        assertTrue(inserted);
        assertNotEquals(0, service.getId());

        final Optional<Service> foundService = serviceDao.findByKey(service.getId());
        assertTrue(foundService.isPresent());
        assertEquals(service, foundService.get());
    }

    @Test
    public void testFindAll() throws DBException {
        final ServiceDao serviceDao = getServiceDao();
        serviceDao.setConnection(getConnectionSupplier().get());

        final List<Service> serviceList = DaoTestData.getServiceStream().collect(Collectors.toList());
        for (var service : serviceList) {
            serviceDao.insert(service);
        }

        final List<Service> found = serviceDao.findAll();
        assertTrue(found.containsAll(serviceList));
    }

    private ServiceDao getServiceDao() {
        return new PostgresServiceDao();
    }

    private ConnectionSupplier getConnectionSupplier() {
        return DaoTestUtil.getPostgresConnectionSupplier();
    }
}
