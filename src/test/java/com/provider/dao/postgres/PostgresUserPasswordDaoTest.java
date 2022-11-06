package com.provider.dao.postgres;

import com.provider.dao.AbstractUserPasswordDaoTest;
import com.provider.dao.exception.DBException;
import org.junit.jupiter.api.BeforeAll;

public class PostgresUserPasswordDaoTest extends AbstractUserPasswordDaoTest {
    @BeforeAll
    static void init() throws DBException {
        connection = new PostgresTestConnectionSupplier().get();
        userDao = new PostgresUserDao();
        userDao.setConnection(connection);
        userPasswordDao = new PostgresUserPasswordDao();
        userPasswordDao.setConnection(connection);
    }
}
