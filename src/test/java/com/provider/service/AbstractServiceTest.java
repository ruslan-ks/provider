package com.provider.service;

import com.provider.dao.ConnectionSupplier;
import com.provider.dao.DaoFactory;
import com.provider.dao.exception.DBException;
import com.provider.entity.EntityFactory;
import com.provider.entity.SimpleEntityFactory;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;

import static org.mockito.Mockito.*;

abstract class AbstractServiceTest {
    protected EntityFactory entityFactory = SimpleEntityFactory.newInstance();

    @Mock
    protected DaoFactory daoFactory;

    @Mock
    private ConnectionSupplier connectionSupplier;

    @Mock
    private Connection connection;

    @BeforeEach
    public void prepare() throws DBException {
        MockitoAnnotations.openMocks(this);
        when(connectionSupplier.get()).thenReturn(connection);
        when(daoFactory.getConnectionSupplier()).thenReturn(connectionSupplier);
    }
}