package com.provider.controller.command.impl;

import com.provider.constants.Paths;
import com.provider.constants.attributes.RequestAttributes;
import com.provider.controller.command.MemberCommand;
import com.provider.controller.command.result.CommandResult;
import com.provider.controller.command.result.CommandResultImpl;
import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import com.provider.entity.user.UserAccount;
import com.provider.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserPanelPageCommand extends MemberCommand {
    UserPanelPageCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected @NotNull CommandResult executeAccessed(@NotNull User user) throws DBException {
        final AccountService accountService = serviceFactory.getAccountService();
        final List<UserAccount> userAccountList = accountService.findUserAccounts(user.getId());
        request.setAttribute(RequestAttributes.USER_ACCOUNTS, userAccountList);
        return CommandResultImpl.of(Paths.USER_PANEL_JSP);
    }
}
