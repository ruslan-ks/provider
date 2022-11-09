package com.provider.controller.command.impl;

import com.provider.constants.Paths;
import com.provider.constants.attributes.SessionAttributes;
import com.provider.controller.command.result.CommandResult;
import com.provider.controller.command.result.CommandResultImpl;
import com.provider.entity.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SignOutCommandTest extends AbstractCommandTest {
    @ParameterizedTest
    @MethodSource("com.provider.TestData#userStream")
    public void testExecuteAuthorized(User user) {
        final HttpSession session = mock(HttpSession.class);
        final HttpServletRequest request = MockUtil.mockRequestWithSession(session);

        final SignOutCommand command = new SignOutCommand(request, response);

        final CommandResult expectedResult = CommandResultImpl.of(Paths.SIGN_IN_JSP);
        assertEquals(expectedResult, command.executeAuthorized(user));

        verify(session).removeAttribute(SessionAttributes.SIGNED_USER);
    }
}