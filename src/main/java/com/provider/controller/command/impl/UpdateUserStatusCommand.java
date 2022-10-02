package com.provider.controller.command.impl;

import com.provider.constants.Paths;
import com.provider.constants.params.UserParams;
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

public class UpdateUserStatusCommand extends AdminCommand {
    public UpdateUserStatusCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected CommandResult executeAccessed(@NotNull User user)
            throws DBException, CommandParamException {
        final Map<String, String> paramMap = getRequestParams(UserParams.ID, UserParams.STATUS);
        final UserService userService = serviceFactory.getUserService();
        final long userId = CommandUtil.parseLongParam(paramMap.get(UserParams.ID));
        final User.Status newStatus = CommandUtil.parseUserStatusParam(paramMap.get(UserParams.STATUS));

        // TODO: add header messages
        if (userService.updateUserStatus(userId, newStatus)) {
            // success
        } else {
            // fail
        }
        return CommandResultImpl.of(Paths.USERS_MANAGEMENT_PAGE);
    }
}
