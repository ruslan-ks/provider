package com.provider.service.impl;

import com.provider.dao.exception.DBException;
import com.provider.service.AbstractAccountServiceTest;
import org.junit.jupiter.api.BeforeEach;

public class AccountServiceImplTest extends AbstractAccountServiceTest {
    @BeforeEach
    public void init() throws DBException {
        accountService = new AccountServiceImpl(daoFactory);
    }
}
