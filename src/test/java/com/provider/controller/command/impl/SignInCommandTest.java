package com.provider.controller.command.impl;

import com.provider.constants.Messages;
import com.provider.constants.Paths;
import com.provider.constants.params.SignInParams;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.controller.command.result.CommandResultImpl;
import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import com.provider.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SignInCommandTest extends AbstractCommandTest {
    @ParameterizedTest
    @MethodSource("com.provider.TestData#userStream")
    public void testPositiveSignIn(User user) throws DBException, ServletException, CommandParamException, IOException {
        final CommandResult expectedResult = CommandResultImpl.of(Paths.USER_PANEL_PAGE);
        testSignIn(user, true, u -> true, expectedResult);
    }

    @ParameterizedTest
    @MethodSource("com.provider.TestData#userStream")
    public void testSuspendedSignIn(User user) throws DBException, ServletException, CommandParamException, IOException {
        final CommandResult expectedResult = CommandResultImpl.of(Paths.SIGN_IN_JSP)
                .addMessage(CommandResult.MessageType.FAIL, Messages.YOU_WERE_SUSPENDED);
        testSignIn(user, true, (u) -> false, expectedResult);
    }

    @ParameterizedTest
    @MethodSource("com.provider.TestData#userStream")
    public void testNegativeSignIn(User user) throws DBException, ServletException, CommandParamException, IOException {
        final CommandResult expectedResult = CommandResultImpl.of(Paths.SIGN_IN_JSP)
                .addMessage(CommandResult.MessageType.FAIL, Messages.INVALID_LOGIN_OR_PASS);
        testSignIn(user, false, (u) -> false, expectedResult);
    }

    private void testSignIn(User user, boolean authenticated, Predicate<User> isActiveUserServiceCheck,
                            CommandResult expectedResult)
            throws DBException, ServletException, CommandParamException, IOException {
        final String password = "pass_" + user;
        final Map<String, String> paramMap = Map.of(
                SignInParams.LOGIN, user.getLogin(),
                SignInParams.PASSWORD, password
        );
        final HttpServletRequest request = MockUtil.mockRequestWithParams(paramMap);
        when(request.getSession()).thenReturn(mock(HttpSession.class));

        final UserService userService = mock(UserService.class);
        when(userService.authenticate(user.getLogin(), password))
                .thenReturn(authenticated
                    ? Optional.of(user)
                    : Optional.empty());
        when(userService.isActiveUser(user)).thenReturn(isActiveUserServiceCheck.test(user));
        when(serviceFactory.getUserService()).thenReturn(userService);

        final SignInCommand signInCommand = new SignInCommand(request, response);
        signInCommand.setServiceFactory(serviceFactory);

        assertEquals(expectedResult, signInCommand.execute());
    }
}