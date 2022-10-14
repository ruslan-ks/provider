package com.provider.controller.command.impl;

import com.provider.constants.Paths;
import com.provider.constants.attributes.RequestAttributes;
import com.provider.constants.params.PaginationParams;
import com.provider.controller.command.AdminCommand;
import com.provider.controller.command.CommandUtil;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.controller.command.result.CommandResultImpl;
import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import com.provider.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class UsersManagementPageCommand extends AdminCommand {
    UsersManagementPageCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected CommandResult executeAccessed(@NotNull User user) throws DBException, CommandParamException {
        // Extract page number parameter
        final Optional<String> pageIndexParam = Optional.ofNullable(request.getParameter(PaginationParams.PAGE_NUMBER));
        final String pageIndexString = pageIndexParam.orElse("1");
        final int pageNumber = CommandUtil.parseIntParam(pageIndexString);
        if (pageNumber < 0) {
            throw new CommandParamException("Invalid parameter:" + PaginationParams.PAGE_NUMBER + "(" + pageNumber +
                    ") < 0");
        }

        // Extract page size parameter
        final Optional<String> pageSizeParam = Optional.ofNullable(request.getParameter(PaginationParams.PAGE_SIZE));
        final String pageSizeString = pageSizeParam.orElse("5");
        final int pageSize = CommandUtil.parseIntParam(pageSizeString);
        if (pageSize < 1) {
            throw new CommandParamException("Invalid parameter:" + PaginationParams.PAGE_SIZE + "(" + pageSize +
                    ") < 1");
        }

        final UserService userService = serviceFactory.getUserService();

        final long offset = (long) (pageNumber - 1) * pageSize;
        final Iterable<User> users = userService.findUsersRange(offset, pageSize);
        request.setAttribute(RequestAttributes.USERS, users);

        final int pageCount = (int) Math.ceil((double) userService.getUsersCount() / pageSize);
        request.setAttribute(RequestAttributes.PAGE_COUNT, pageCount);

        return CommandResultImpl.of(Paths.USERS_MANAGEMENT_JSP);
    }
}
