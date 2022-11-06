package com.provider.dao;

import com.provider.entity.EntityFactory;
import com.provider.entity.SimpleEntityFactory;

import java.sql.Connection;

public abstract class AbstractDaoTest {
    protected static final EntityFactory entityFactory = SimpleEntityFactory.newInstance();

    /**
     * Returns connection depending on DB that is used.<br>
     * Dao tests using this method <strong>MUST NOT CLOSE OBTAINED CONNECTION via .close() method call</strong>.
     * This should be implemented via @BeforeEach and @AfterEach
     * @return connection instance
     */
    protected abstract Connection getConnection();
}
