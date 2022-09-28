package com.provider.controller.command.impl;

import com.provider.constants.Paths;
import com.provider.constants.params.ReplenishParams;
import com.provider.controller.command.CommandUtil;
import com.provider.controller.command.MemberCommand;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.controller.command.result.CommandResultImpl;
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
    protected CommandResult executeAccessed(@NotNull User user) throws DBException, CommandParamException {
        final Map<String, String> paramMap = getRequestParams(ReplenishParams.CURRENCY);
        final Currency accountCurrency = CommandUtil.parseCurrencyParam(paramMap.get(ReplenishParams.CURRENCY));

        final AccountService accountService = serviceFactory.getAccountService();
        final Optional<UserAccount> account = accountService.findUserAccount(user, accountCurrency);

        logger.debug("ReplenishPageCommand: user: {}, currency: {}, account: {}", user, accountCurrency, account);

        if (account.isPresent() && accountService.isUserAccount(account.get(), user)) {
            request.setAttribute(ReplenishParams.CURRENCY, account.get().getCurrency());
            return CommandResultImpl.of(Paths.REPLENISH_JSP);
        }
        // There is no requested account that belongs to the signed user
        throw new CommandParamException();
    }
}
