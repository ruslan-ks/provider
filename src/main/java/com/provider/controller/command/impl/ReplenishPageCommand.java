package com.provider.controller.command.impl;

import com.provider.constants.Paths;
import com.provider.constants.params.ReplenishParams;
import com.provider.controller.command.CommandUtil;
import com.provider.controller.command.MemberCommand;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.dao.exception.DBException;
import com.provider.entity.Currency;
import com.provider.entity.user.User;
import com.provider.entity.user.UserAccount;
import com.provider.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

public class ReplenishPageCommand extends MemberCommand {
    private static final Logger logger = LoggerFactory.getLogger(ReplenishPageCommand.class);

    ReplenishPageCommand(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected @NotNull CommandResult executeAuthorized(@NotNull User user) throws DBException, CommandParamException {
        final Map<String, String> paramMap = getRequiredParams(ReplenishParams.CURRENCY);
        final Currency accountCurrency = CommandUtil.parseCurrencyParam(paramMap.get(ReplenishParams.CURRENCY));

        final AccountService accountService = serviceFactory.getAccountService();
        final Optional<UserAccount> account = accountService.findUserAccount(user, accountCurrency);

        logger.trace("ReplenishPageCommand: user: {}, currency: {}, account: {}", user, accountCurrency, account);

        if (account.isEmpty()) {
            logger.warn("Failed to find user account: user: {}, currency: {}", user, accountCurrency);
            throw new CommandParamException("Failed to find user account");
        }
        if (!accountService.isUserAccount(account.get(), user)) {
            logger.warn("Account does not belong to the signed user! user: {}, account: {}", user, account.get());
            throw new CommandParamException("Account does not belong to the signed user!");
        }
        request.setAttribute(ReplenishParams.CURRENCY, account.get().getCurrency());
        return newCommandResult(Paths.REPLENISH_JSP);
    }
}
