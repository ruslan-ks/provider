package com.provider.controller.command.impl;

import com.provider.entity.user.User;
import com.provider.entity.user.impl.UserImpl;
import com.provider.service.ServiceFactory;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public abstract class AbstractCommandTest {
    protected static final User ADMIN = UserImpl.of(0, "Test", "Admin", "CommandTestAdmin",
            "", User.Role.ADMIN, User.Status.ACTIVE);

    protected static final User MEMBER = UserImpl.of(0, "Test", "Member", "CommandTestMember",
            "", User.Role.MEMBER, User.Status.ACTIVE);

    @Mock
    protected HttpServletResponse response;

    @Mock
    protected ServiceFactory serviceFactory;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.openMocks(this);
    }
}
