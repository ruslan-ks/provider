package com.provider.controller.command.impl;

import com.provider.constants.Messages;
import com.provider.constants.Paths;
import com.provider.constants.params.TariffParams;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.controller.command.result.CommandResultImpl;
import com.provider.dao.exception.DBException;
import com.provider.entity.product.Tariff;
import com.provider.entity.user.User;
import com.provider.entity.user.UserAccount;
import com.provider.service.AccountService;
import com.provider.service.SubscriptionService;
import com.provider.service.TariffService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.Nested;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubscribeCommandTest extends AbstractCommandTest {
    @ParameterizedTest
    @MethodSource("com.provider.TestData#tariffStream")
    public void testPositiveExecuteAuthorized(Tariff tariff)
            throws DBException, ServletException, CommandParamException, IOException {
        final CommandResult expectedResult = CommandResultImpl.of(Paths.USER_PANEL_PAGE)
                .addMessage(CommandResult.MessageType.SUCCESS, Messages.BUY_SUBSCRIPTION_SUCCESS);
        testSubscribe(tariff, false, true, true, expectedResult);
    }

    @Nested
    class Negative {
        @ParameterizedTest
        @MethodSource("com.provider.TestData#tariffStream")
        public void testActiveSubscriptionExists(Tariff tariff)
                throws DBException, ServletException, CommandParamException, IOException {
            final CommandResult expectedResult = CommandResultImpl.of(Paths.USER_PANEL_PAGE)
                    .addMessage(CommandResult.MessageType.FAIL, Messages.SUBSCRIPTION_ALREADY_EXISTS);
            testSubscribe(tariff, true, true, true, expectedResult);
        }

        @ParameterizedTest
        @MethodSource("com.provider.TestData#tariffStream")
        public void testNotEnoughMoney(Tariff tariff)
                throws DBException, ServletException, CommandParamException, IOException {
            final CommandResult expectedResult = CommandResultImpl.of(Paths.USER_PANEL_PAGE)
                    .addMessage(CommandResult.MessageType.FAIL, Messages.NOT_ENOUGH_MONEY);
            testSubscribe(tariff, false, false, true, expectedResult);
        }

        @ParameterizedTest
        @MethodSource("com.provider.TestData#tariffStream")
        public void testFailToSubscribe(Tariff tariff)
                throws DBException, ServletException, CommandParamException, IOException {
            final CommandResult expectedResult = CommandResultImpl.of(Paths.USER_PANEL_PAGE)
                    .addMessage(CommandResult.MessageType.FAIL, Messages.BUY_SUBSCRIPTION_FAIL);
            testSubscribe(tariff, false, true, false, expectedResult);
        }
    }

    private void testSubscribe(Tariff tariff, boolean activeSubscriptionExists, boolean hasEnoughMoney,
                               boolean isSuccess, CommandResult expectedCommandResult)
            throws DBException, ServletException, CommandParamException, IOException {
        final Map<String, String> paramMap = Map.of(TariffParams.ID, String.valueOf(tariff.getId()));
        final HttpServletRequest request = MockUtil.mockRequestWithParams(paramMap);

        final TariffService tariffService = mock(TariffService.class);
        when(tariffService.findTariffById(tariff.getId())).thenReturn(Optional.of(tariff));
        when(serviceFactory.getTariffService()).thenReturn(tariffService);

        final AccountService accountService = mock(AccountService.class);
        final UserAccount account = mock(UserAccount.class);
        final User user = mock(User.class);
        when(accountService.findUserAccount(user)).thenReturn(account);
        when(serviceFactory.getAccountService()).thenReturn(accountService);

        final SubscriptionService subscriptionService = mock(SubscriptionService.class);
        when(subscriptionService.activeSubscriptionExists(account, tariff)).thenReturn(activeSubscriptionExists);
        when(subscriptionService.hasEnoughMoneyToPay(account, tariff)).thenReturn(hasEnoughMoney);
        when(subscriptionService.buySubscription(account, tariff)).thenReturn(isSuccess);
        when(serviceFactory.getSubscriptionService()).thenReturn(subscriptionService);

        final SubscribeCommand command = new SubscribeCommand(request, response);
        command.setServiceFactory(serviceFactory);

        assertEquals(expectedCommandResult, command.executeAuthorized(user));

        if (!activeSubscriptionExists && hasEnoughMoney) {
            verify(subscriptionService).buySubscription(account, tariff);
        } else {
            verify(subscriptionService, never()).buySubscription(account, tariff);
        }
    }
}