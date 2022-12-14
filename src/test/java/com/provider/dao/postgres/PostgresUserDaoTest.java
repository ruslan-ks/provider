package com.provider.dao.postgres;

import com.provider.dao.*;
import com.provider.dao.exception.DBException;
import org.junit.jupiter.api.BeforeAll;

public class PostgresUserDaoTest extends AbstractUserDaoTest {
    @BeforeAll
    public static void init() throws DBException {
        connection = new PostgresTestConnectionSupplier().get();
        userDao = new PostgresUserDao();
        userDao.setConnection(connection);
    }
}
