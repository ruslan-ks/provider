package com.provider.dao.postgres;

import com.provider.dao.AbstractTariffDaoTest;
import com.provider.dao.exception.DBException;
import org.junit.jupiter.api.BeforeAll;

public class PostgresTariffDaoTest extends AbstractTariffDaoTest {
    @BeforeAll
    public static void init() throws DBException {
        connection = new PostgresTestConnectionSupplier().get();
        tariffDao = new PostgresTariffDao();
        tariffDao.setConnection(connection);
        serviceDao = new PostgresServiceDao();
        serviceDao.setConnection(connection);
    }
}
