package com.provider.service.impl;

import com.provider.dao.exception.DBException;
import com.provider.service.AbstractUserServiceTest;
import org.junit.jupiter.api.BeforeEach;

public class UserServiceImplTest extends AbstractUserServiceTest {
    @BeforeEach
    public void init() throws DBException {
        userService = UserServiceImpl.newInstance(daoFactory);
    }
}
