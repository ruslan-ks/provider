package com.provider.dao;

import java.sql.Connection;

public abstract class AbstractDaoTest {
     /**
     * Returns connection depending on DB that is used.<br>
     * Dao tests using this method <strong>MUST NOT CLOSE OBTAINED CONNECTION via .close() method call</strong>.
     * This should be implemented via @BeforeEach and @AfterEach
     * @return connection instance
     */
    protected abstract Connection getConnection();
}
