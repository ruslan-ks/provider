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
import java.util.Map;
import java.util.Optional;

public class ReplenishCommand extends MemberAccessCommand {
    ReplenishCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected void executeAccessed(@NotNull User user) throws DBException, CommandParamException, IOException {
        final Map<String, String> paramMap = getRequestParams(ReplenishParams.ACCOUNT_ID, ReplenishParams.AMOUNT);

        final long accountId = CommandUtil.parseLongParam(paramMap.get(ReplenishParams.ACCOUNT_ID));
        final BigDecimal amount = CommandUtil.parseBigDecimalParam(paramMap.get(ReplenishParams.AMOUNT));
        if (!isValidAmount(amount)) {
            throw new CommandParamException("Invalid amount: " + amount);
        }

        final AccountService accountService = AccountServiceImpl.newInstance();
        final Optional<UserAccount> userAccount = accountService.findAccount(accountId);

        if (userAccount.isPresent() && accountService.isUserAccount(userAccount.get(), user)) {
            accountService.replenish(userAccount.get(), amount);
            response.sendRedirect(Paths.USER_PANEL_PAGE);
        } else {
            throw new CommandParamException("Illegal account id: " + accountId);
        }
    }

    private static boolean isValidAmount(@NotNull BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }
}
