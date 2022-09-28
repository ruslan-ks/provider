package com.provider.controller.command;

import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import com.provider.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

public abstract class AdminCommand extends MemberCommand {
    public AdminCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected boolean hasAccessRights(@NotNull User user) throws DBException {
        final UserService userService = serviceFactory.getUserService();
        // is signed in and has admin rights
        return super.hasAccessRights(user) && userService.hasAdminRights(user);
    }
}
