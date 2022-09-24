package com.provider.controller.command.impl;

import com.provider.constants.Paths;
import com.provider.constants.params.ReplenishParams;
import com.provider.controller.command.CommandUtil;
import com.provider.controller.command.MemberAccessCommand;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import com.provider.entity.user.UserAccount;
import com.provider.service.AccountService;
import com.provider.service.AccountServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

public class ReplenishCommand extends MemberAccessCommand {
    ReplenishCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected void executeAccessed() throws DBException, CommandParamException, IOException {
        final String accountIdParam = request.getParameter(ReplenishParams.ACCOUNT_ID);
        final String amountParam = request.getParameter(ReplenishParams.AMOUNT);

        CommandUtil.throwIfNullParam(accountIdParam, amountParam);

        final long accountId = CommandUtil.parseLongParam(accountIdParam);
        final BigDecimal amount = CommandUtil.parseBigDecimalParam(amountParam);
        if (!isValidAmount(amount)) {
            throw new IllegalArgumentException("Invalid amount: " + amount);
        }

        final User user = getSignedInUser().orElseThrow();
        final AccountService accountService = AccountServiceImpl.newInstance();
        final Optional<UserAccount> userAccount = accountService.findAccount(accountId);

        if (userAccount.isPresent() && accountService.isUserAccount(userAccount.get(), user)) {
            accountService.replenish(userAccount.get(), amount);
            response.sendRedirect(Paths.USER_PANEL_PAGE);
            return;
        }
        throw new CommandParamException("Illegal account id: " + accountId);
    }

    private static boolean isValidAmount(@NotNull BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }
}
