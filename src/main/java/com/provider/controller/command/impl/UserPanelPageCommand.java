package com.provider.controller.command.impl;

import com.provider.constants.Paths;
import com.provider.constants.attributes.RequestAttributes;
import com.provider.controller.command.MemberAccessCommand;
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
import java.util.List;

public class UserPanelPageCommand extends MemberAccessCommand {
    UserPanelPageCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected void executeAccessed(@NotNull User user) throws DBException, ServletException, IOException {
        final AccountService accountService = AccountServiceImpl.newInstance();
        final List<UserAccount> userAccountList = accountService.findUserAccounts(user.getId());
        request.setAttribute(RequestAttributes.USER_ACCOUNTS, userAccountList);
        request.getRequestDispatcher(Paths.USER_PANEL_JSP).forward(request, response);
    }
}