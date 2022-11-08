package com.provider.controller.command.impl;

import com.provider.constants.Messages;
import com.provider.constants.Paths;
import com.provider.constants.params.ReplenishParams;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.controller.command.result.CommandResultImpl;
import com.provider.dao.exception.DBException;
import com.provider.entity.Currency;
import com.provider.entity.user.User;
import com.provider.entity.user.UserAccount;
import com.provider.entity.user.impl.UserAccountImpl;
import com.provider.service.AccountService;
import com.provider.service.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReplenishCommandTest extends AbstractCommandTest {
    @ParameterizedTest
    @MethodSource("com.provider.TestData#userStream")
    public void testPositiveExecuteAuthorized(User user) throws DBException, ValidationException, CommandParamException {
        final Currency currency = Currency.USD;
        final int amount = 1000;
        final UserAccount account = UserAccountImpl.of(0, user.getId(), currency);
        final Map<String, String> paramMap = Map.of(
                ReplenishParams.CURRENCY, currency.name(),
                ReplenishParams.AMOUNT, String.valueOf(amount)
        );
        final HttpServletRequest request = MockUtil.mockRequestWithParams(paramMap);

        final AccountService accountService = mock(AccountService.class);
        when(accountService.findUserAccount(user, currency)).thenReturn(Optional.of(account));
        when(accountService.replenish(account, BigDecimal.valueOf(amount))).thenReturn(true);
        when(serviceFactory.getAccountService()).thenReturn(accountService);

        final ReplenishCommand command = new ReplenishCommand(request, response);
        command.setServiceFactory(serviceFactory);

        final CommandResult expectedResult = CommandResultImpl.of(Paths.USER_PANEL_PAGE)
                .addMessage(CommandResult.MessageType.SUCCESS, Messages.ACCOUNT_REPLENISH_SUCCESS);
        assertEquals(expectedResult, command.executeAuthorized(user));

        verify(accountService, times(1)).replenish(account, BigDecimal.valueOf(amount));
    }

    @ParameterizedTest
    @MethodSource("com.provider.TestData#userStream")
    public void testNegativeExecuteAuthorized(User user) throws DBException, ValidationException, CommandParamException {
        final Currency currency = Currency.USD;
        final int amount = 1000;
        final UserAccount account = UserAccountImpl.of(0, user.getId(), currency);
        final Map<String, String> paramMap = Map.of(
                ReplenishParams.CURRENCY, currency.name(),
                ReplenishParams.AMOUNT, String.valueOf(amount)
        );
        final HttpServletRequest request = MockUtil.mockRequestWithParams(paramMap);

        final AccountService accountService = mock(AccountService.class);
        when(accountService.findUserAccount(user, currency)).thenReturn(Optional.of(account));
        when(accountService.replenish(account, BigDecimal.valueOf(amount))).thenReturn(false);
        when(serviceFactory.getAccountService()).thenReturn(accountService);

        final ReplenishCommand command = new ReplenishCommand(request, response);
        command.setServiceFactory(serviceFactory);

        final CommandResult expectedResult = CommandResultImpl.of(Paths.USER_PANEL_PAGE)
                .addMessage(CommandResult.MessageType.FAIL, Messages.ACCOUNT_REPLENISH_FAIL);
        assertEquals(expectedResult, command.executeAuthorized(user));

        verify(accountService, times(1)).replenish(account, BigDecimal.valueOf(amount));
    }
}