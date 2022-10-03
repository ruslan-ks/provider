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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class UpdateUserStatusCommand extends AdminCommand {
    private static final Logger logger = LoggerFactory.getLogger(UpdateUserStatusCommand.class);

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

        final CommandResult commandResult = CommandResultImpl.of(Paths.USERS_MANAGEMENT_PAGE);
        if (userService.updateUserStatus(userId, newStatus)) {
            logger.debug("Adding success message");
            commandResult.addMessage(CommandResult.MessageType.SUCCESS, "User status changed successfully");
        } else {
            logger.debug("Adding error message");
            commandResult.addMessage(CommandResult.MessageType.FAIL, "Failed to change user status");
        }
        return commandResult;
    }
}
