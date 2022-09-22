package com.provider.controller.command.impl;

import com.provider.constants.Paths;
import com.provider.constants.attributes.RequestAttributes;
import com.provider.constants.attributes.SessionAttributes;
import com.provider.controller.command.FrontCommand;
import com.provider.controller.command.exception.CommandAccessException;
import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import com.provider.entity.user.UserAccount;
import com.provider.service.UserService;
import com.provider.service.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserPanelCommand extends FrontCommand {
    UserPanelCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response)
            throws CommandAccessException {
        super(request, response);
    }

    @Override
    public void execute() throws DBException, ServletException, IOException {
        final Optional<HttpSession> session = getSession();
        final Optional<User> user = session.map(s -> (User) s.getAttribute(SessionAttributes.SIGNED_USER));
        if (user.isPresent()) {
            final UserService userService = UserServiceImpl.newInstance();
            final List<UserAccount> userAccountList = userService.findAllUserAccounts(user.get().getId());
            request.setAttribute(RequestAttributes.USER_ACCOUNTS, userAccountList);
            request.getRequestDispatcher(Paths.USER_PANEL_PAGE).forward(request, response);
        } else {
            request.getRequestDispatcher(Paths.SIGN_IN_PAGE).forward(request, response);
        }
    }
}
