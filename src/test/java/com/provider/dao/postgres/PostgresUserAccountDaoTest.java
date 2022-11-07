package com.provider.dao.postgres;

import com.provider.dao.AbstractUserAccountDaoTest;
import com.provider.dao.exception.DBException;
import org.junit.jupiter.api.BeforeAll;

public class PostgresUserAccountDaoTest extends AbstractUserAccountDaoTest {
    @BeforeAll
    public static void init() throws DBException {
        connection = new PostgresTestConnectionSupplier().get();
        userDao = new PostgresUserDao();
        userDao.setConnection(connection);
        userAccountDao = new PostgresUserAccountDao();
        userAccountDao.setConnection(connection);
    }
}
