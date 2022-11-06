package com.provider.dao.postgres;

import com.provider.dao.*;
import com.provider.dao.exception.DBException;
import org.junit.jupiter.api.BeforeAll;

public class PostgresServiceDaoTest extends AbstractServiceDaoTest {
    @BeforeAll
    public static void init() throws DBException {
        connection = new PostgresTestConnectionSupplier().get();
        serviceDao = new PostgresServiceDao();
        serviceDao.setConnection(connection);
        tariffDao = new PostgresTariffDao();
        tariffDao.setConnection(connection);
    }
}
