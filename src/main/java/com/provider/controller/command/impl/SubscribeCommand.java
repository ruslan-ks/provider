package com.provider.controller.command.impl;

import com.provider.constants.Messages;
import com.provider.constants.Paths;
import com.provider.constants.params.TariffParams;
import com.provider.controller.command.CommandUtil;
import com.provider.controller.command.MemberCommand;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.dao.exception.DBException;
import com.provider.entity.product.Tariff;
import com.provider.entity.user.User;
import com.provider.entity.user.UserAccount;
import com.provider.service.AccountService;
import com.provider.service.SubscriptionService;
import com.provider.service.TariffService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SubscribeCommand extends MemberCommand {
    private static final Logger logger = LoggerFactory.getLogger(SubscribeCommand.class);

    public SubscribeCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected @NotNull CommandResult executeAuthorized(@NotNull User user)
            throws DBException, ServletException, IOException, CommandParamException {
        final int tariffId = CommandUtil.parseIntParam(getRequiredParam(TariffParams.ID));

        final TariffService tariffService = serviceFactory.getTariffService();
        final Tariff tariff = tariffService.findTariffById(tariffId)
                .orElseThrow(CommandParamException::new);

        final AccountService accountService = serviceFactory.getAccountService();
        final UserAccount userAccount = accountService.findUserAccount(user)
                .orElseThrow(() -> new RuntimeException("Failed to obtain user account! user: " + user));

        final SubscriptionService subscriptionService = serviceFactory.getSubscriptionService();

        if (subscriptionService.activeSubscriptionExists(userAccount, tariff)) {
            logger.warn("Failed to subscribe! Active subscription already exists!  user: {}, account: {}, tariff: {}",
                    user, userAccount, tariff);
            final CommandResult subscriptionAlreadyExistsResult = newCommandResult(Paths.USER_PANEL_PAGE);
            subscriptionAlreadyExistsResult.addMessage(CommandResult.MessageType.FAIL, Messages.SUBSCRIPTION_ALREADY_EXISTS);
            return subscriptionAlreadyExistsResult;
        }

        if (!subscriptionService.hasEnoughMoneyToPay(userAccount, tariff)) {
            logger.warn("Failed to subscribe! User does not have enough money! user: {}, account: {}, tariff: {}",
                    user, userAccount, tariff);
            final CommandResult notEnoughMoneyResult = newCommandResult(Paths.USER_PANEL_PAGE);
            notEnoughMoneyResult.addMessage(CommandResult.MessageType.FAIL, Messages.NOT_ENOUGH_MONEY);
            return notEnoughMoneyResult;
        }

        final boolean bought = subscriptionService.buySubscription(userAccount, tariff);
        final CommandResult commandResult = newCommandResult(Paths.USER_PANEL_PAGE);
        if (bought) {
            logger.info("Subscription added: user: {}, userAccount: {}, tariff: {}", user, userAccount, tariff);
            commandResult.addMessage(CommandResult.MessageType.SUCCESS, Messages.BUY_SUBSCRIPTION_SUCCESS);
        } else {
            commandResult.addMessage(CommandResult.MessageType.FAIL, Messages.BUY_SUBSCRIPTION_FAIL);
        }
        return commandResult;
    }
}
