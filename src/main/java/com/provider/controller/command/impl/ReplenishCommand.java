package com.provider.controller.command.impl;

import com.provider.constants.Messages;
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
import com.provider.service.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

public class ReplenishCommand extends MemberCommand {
    private static final Logger logger = LoggerFactory.getLogger(ReplenishCommand.class);

    ReplenishCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected @NotNull CommandResult executeAuthorized(@NotNull User user) throws DBException, CommandParamException {
        final Map<String, String> paramMap = getRequiredParams(ReplenishParams.CURRENCY, ReplenishParams.AMOUNT);

        final Currency accountCurrency = CommandUtil.parseCurrencyParam(paramMap.get(ReplenishParams.CURRENCY));
        final BigDecimal amount = CommandUtil.parseBigDecimalParam(paramMap.get(ReplenishParams.AMOUNT));

        final AccountService accountService = serviceFactory.getAccountService();
        final Optional<UserAccount> userAccount = accountService.findUserAccount(user, accountCurrency);

        if (userAccount.isEmpty()) {
            logger.warn("Failed to find user account: user: {}, currency: {}", user, accountCurrency);
            throw new CommandParamException("Failed to find user account");
        }
        if (!accountService.isUserAccount(userAccount.get(), user)) {
            logger.warn("Account does not belong to the signed user! user: {}, account: {}", user, userAccount.get());
            throw new CommandParamException("Account does not belong to the signed user!");
        }

        final CommandResult commandResult = newCommandResult(Paths.USER_PANEL_PAGE);
        boolean replenished = false;
        try {
            replenished = accountService.replenish(userAccount.get(), amount);
        } catch (ValidationException ex) {
            logger.warn("Validation exception: ", ex);
            commandResult.addMessage(CommandResult.MessageType.FAIL, Messages.INVALID_REPLENISH_AMOUNT);
        }
        if (replenished) {
            commandResult.addMessage(CommandResult.MessageType.SUCCESS, Messages.ACCOUNT_REPLENISH_SUCCESS);
        } else {
            commandResult.addMessage(CommandResult.MessageType.FAIL, Messages.ACCOUNT_REPLENISH_FAIL);
        }
        return commandResult;
    }
}
