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
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class ReplenishPageCommand extends MemberAccessCommand {
    ReplenishPageCommand(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected void executeAccessed(@NotNull User user)
            throws DBException, ServletException, IOException, CommandParamException {
        final Map<String, String> paramMap = getRequestParams(ReplenishParams.ACCOUNT_ID);
        final long accountId = CommandUtil.parseLongParam(paramMap.get(ReplenishParams.ACCOUNT_ID));

        final AccountService accountService = AccountServiceImpl.newInstance();
        final Optional<UserAccount> userAccount = accountService.findAccount(accountId);

        if (userAccount.isPresent() && accountService.isUserAccount(userAccount.get(), user)) {
            request.setAttribute(ReplenishParams.CURRENCY, userAccount.get().getCurrency());
            request.getRequestDispatcher(Paths.REPLENISH_JSP).forward(request, response);
        } else {
            // There is no requested account that belongs to the signed user
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
