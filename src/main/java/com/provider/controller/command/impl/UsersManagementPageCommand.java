package com.provider.controller.command.impl;

import com.provider.constants.Paths;
import com.provider.constants.attributes.RequestAttributes;
import com.provider.constants.params.UsersManagementParams;
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

import java.util.Map;
import java.util.Optional;

public class UsersManagementPageCommand extends AdminCommand {
    public UsersManagementPageCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected CommandResult executeAccessed(@NotNull User user) throws DBException, CommandParamException {
        final Map<String, String[]> params = request.getParameterMap();
        final Optional<String> pageIndexParam = Optional.ofNullable(params.get(UsersManagementParams.PAGE_NUMBER))
                .map(arr -> arr[0]);

        final String pageIndexString = pageIndexParam.orElse("1");
        final int pageNumber = CommandUtil.parseIntParam(pageIndexString);
        if (pageNumber < 0) {
            throw new CommandParamException("Invalid parameter: page number(" + pageNumber + ") < 0");
        }

        final int step = 5;
        final long offset = (long) (pageNumber - 1) * step;

        final UserService userService = serviceFactory.getUserService();
        final Iterable<User> users = userService.findUsersRange(offset, step);
        request.setAttribute(RequestAttributes.USERS, users);

        final int pageCount = (int) Math.ceil((double) userService.getUsersCount() / step);
        request.setAttribute(RequestAttributes.PAGE_COUNT, pageCount);

        return CommandResultImpl.of(Paths.USERS_MANAGEMENT_JSP);
    }
}
