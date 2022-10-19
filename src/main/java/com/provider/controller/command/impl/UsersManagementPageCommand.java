package com.provider.controller.command.impl;

import com.provider.constants.Paths;
import com.provider.constants.attributes.RequestAttributes;
import com.provider.controller.command.AdminCommand;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.controller.command.result.CommandResultImpl;
import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import com.provider.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UsersManagementPageCommand extends AdminCommand {
    UsersManagementPageCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected @NotNull CommandResult executeAuthorized(@NotNull User user) throws DBException, CommandParamException {
        final PaginationHelper paginationHelper = getPaginationHelper();
        final int pageSize = paginationHelper.getPageSize();
        final long offset = paginationHelper.getOffset();

        final UserService userService = serviceFactory.getUserService();

        final List<User> users = userService.findUsersPage(offset, pageSize);
        request.setAttribute(RequestAttributes.USERS, users);

        final long userCount = userService.getUsersCount();
        paginationHelper.setPageCountAttribute(userCount);

        return CommandResultImpl.of(Paths.USERS_MANAGEMENT_JSP);
    }
}
