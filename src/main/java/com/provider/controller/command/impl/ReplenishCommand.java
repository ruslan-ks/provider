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
import com.provider.service.AccountServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

public class ReplenishCommand extends MemberCommand {
    ReplenishCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected CommandResult executeAccessed(@NotNull User user) throws DBException, CommandParamException {
        final Map<String, String> paramMap = getRequestParams(ReplenishParams.CURRENCY, ReplenishParams.AMOUNT);

        final Currency accountCurrency = CommandUtil.parseCurrencyParam(paramMap.get(ReplenishParams.CURRENCY));
        final BigDecimal amount = CommandUtil.parseBigDecimalParam(paramMap.get(ReplenishParams.AMOUNT));
        if (!isValidAmount(amount)) {
            throw new CommandParamException("Invalid amount: " + amount);
        }

        final AccountService accountService = AccountServiceImpl.newInstance();
        final Optional<UserAccount> userAccount = accountService.findUserAccount(user, accountCurrency);

        if (userAccount.isPresent() && accountService.isUserAccount(userAccount.get(), user)) {
            accountService.replenish(userAccount.get(), amount);
            return CommandResultImpl.of(Paths.USER_PANEL_PAGE);
        }
        throw new CommandParamException("Account not found");
    }

    private static boolean isValidAmount(@NotNull BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }
}
