package com.provider.controller.command.impl;

import com.provider.constants.Paths;
import com.provider.constants.attributes.RequestAttributes;
import com.provider.controller.command.MemberCommand;
import com.provider.controller.command.result.CommandResult;
import com.provider.controller.command.result.CommandResultImpl;
import com.provider.dao.exception.DBException;
import com.provider.entity.dto.SubscriptionTariffDto;
import com.provider.entity.user.User;
import com.provider.entity.user.UserAccount;
import com.provider.service.AccountService;
import com.provider.service.SubscriptionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserPanelPageCommand extends MemberCommand {
    UserPanelPageCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected @NotNull CommandResult executeAuthorized(@NotNull User user) throws DBException {
        final AccountService accountService = serviceFactory.getAccountService();
        final List<UserAccount> userAccountList = accountService.findUserAccounts(user.getId());
        request.setAttribute(RequestAttributes.USER_ACCOUNTS, userAccountList);

        final UserAccount userUsdAccount = accountService.findUserAccount(user)
                .orElseThrow(() -> new RuntimeException("User account not found! user: " + user));

        final SubscriptionService subscriptionService = serviceFactory.getSubscriptionService();
        final List<SubscriptionTariffDto> userActiveSubscriptionDtoList =
                subscriptionService.findActiveSubscriptionsFullInfo(userUsdAccount, getLocale());
        request.setAttribute(RequestAttributes.USER_ACTIVE_SUBSCRIPTION_DTOS, userActiveSubscriptionDtoList);

        return CommandResultImpl.of(Paths.USER_PANEL_JSP);
    }
}
