package com.provider.controller.command.impl;

import com.provider.constants.Messages;
import com.provider.constants.Paths;
import com.provider.constants.params.TariffParams;
import com.provider.controller.command.CommandUtil;
import com.provider.controller.command.MemberCommand;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.dao.exception.DBException;
import com.provider.entity.product.Subscription;
import com.provider.entity.user.User;
import com.provider.entity.user.UserAccount;
import com.provider.service.AccountService;
import com.provider.service.SubscriptionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UnsubscribeCommand extends MemberCommand {
    private static final Logger logger = LoggerFactory.getLogger(UnsubscribeCommand.class);

    public UnsubscribeCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected @NotNull CommandResult executeAuthorized(@NotNull User user)
            throws DBException, ServletException, IOException, CommandParamException {
        final String tariffIdParam = getRequiredParam(TariffParams.ID);
        final int tariffId = CommandUtil.parseIntParam(tariffIdParam);

        final AccountService accountService = serviceFactory.getAccountService();
        final UserAccount userAccount = accountService.findUserAccount(user);

        final SubscriptionService subscriptionService = serviceFactory.getSubscriptionService();
        final List<Subscription> subscriptionList = subscriptionService.findActiveSubscriptions(userAccount);

        final Optional<Subscription> activeSubscription = subscriptionList.stream()
                .filter(s -> s.getTariffId() == tariffId)
                .findAny();

        final CommandResult commandResult = newCommandResult(Paths.USER_PANEL_PAGE);
        if (activeSubscription.isEmpty()) {
            logger.warn("Failed to unsubscribe! Subscription not found! User: {}, user account: {}, tariff id: {}",
                    user, userAccount, tariffId);
            return commandResult.addMessage(CommandResult.MessageType.FAIL, Messages.UNSUBSCRIBE_FAIL);
        }

        final boolean unsubscribed = subscriptionService.unsubscribe(activeSubscription.get());
        if (unsubscribed) {
            logger.info("Unsubscribed: user: {}, account: {}, subscription: {}", user, userAccount,
                    activeSubscription.get());
            return commandResult.addMessage(CommandResult.MessageType.SUCCESS, Messages.UNSUBSCRIBE_SUCCESS);
        }
        return commandResult.addMessage(CommandResult.MessageType.FAIL, Messages.UNSUBSCRIBE_FAIL);
    }
}
