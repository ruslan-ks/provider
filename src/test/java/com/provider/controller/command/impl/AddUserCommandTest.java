package com.provider.controller.command.impl;

import com.provider.constants.Messages;
import com.provider.constants.Paths;
import com.provider.constants.params.UserParams;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.exception.UserAccessRightsException;
import com.provider.controller.command.result.CommandResult;
import com.provider.controller.command.result.CommandResultImpl;
import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import com.provider.service.UserService;
import com.provider.service.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AddUserCommandTest extends AbstractCommandTest {
    @ParameterizedTest
    @MethodSource("com.provider.TestData#userStream")
    public void testPositiveExecuteAuthorized(User user)
            throws DBException, ValidationException, CommandParamException, UserAccessRightsException {
        final String password = "pass_" + user.getName();
        final Map<String, String> paramMap = Map.of(
                UserParams.LOGIN, user.getLogin(),
                UserParams.PASSWORD, password,
                UserParams.NAME, user.getName(),
                UserParams.SURNAME, user.getSurname(),
                UserParams.PHONE, user.getPhone(),
                UserParams.ROLE, user.getRole().name()
        );
        final HttpServletRequest request = MockUtil.mockRequestWithParams(paramMap);

        final UserService userService = mock(UserService.class);
        when(userService.insertUser(user, password)).thenReturn(true);
        when(serviceFactory.getUserService()).thenReturn(userService);

        final AddUserCommand command = new AddUserCommand(request, response);
        command.setServiceFactory(serviceFactory);
        final CommandResult expectedResult = CommandResultImpl.of(Paths.USERS_MANAGEMENT_PAGE)
                .addMessage(CommandResult.MessageType.SUCCESS, Messages.USER_INSERT_SUCCESS);
        assertEquals(expectedResult, command.executeAuthorized(ADMIN));
    }

    @ParameterizedTest
    @MethodSource("com.provider.TestData#userStream")
    public void testNegativeExecuteAuthorized(User user)
            throws DBException, ValidationException, CommandParamException, UserAccessRightsException {
        final String password = "pass_" + user.getName();
        final Map<String, String> paramMap = Map.of(
                UserParams.LOGIN, user.getLogin(),
                UserParams.PASSWORD, password,
                UserParams.NAME, user.getName(),
                UserParams.SURNAME, user.getSurname(),
                UserParams.PHONE, user.getPhone(),
                UserParams.ROLE, user.getRole().name()
        );
        final HttpServletRequest request = MockUtil.mockRequestWithParams(paramMap);

        final UserService userService = mock(UserService.class);
        when(userService.insertUser(user, password)).thenReturn(false);
        when(serviceFactory.getUserService()).thenReturn(userService);

        final AddUserCommand command = new AddUserCommand(request, response);
        command.setServiceFactory(serviceFactory);
        final CommandResult expectedResult = CommandResultImpl.of(Paths.USERS_MANAGEMENT_PAGE)
                .addMessage(CommandResult.MessageType.FAIL, Messages.USER_INSERT_FAIL);
        assertEquals(expectedResult, command.executeAuthorized(ADMIN));
    }
}
