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
import java.util.NoSuchElementException;

public class UpdateUserStatusCommand extends AdminCommand {
    private static final Logger logger = LoggerFactory.getLogger(UpdateUserStatusCommand.class);

    UpdateUserStatusCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected @NotNull CommandResult executeAuthorized(@NotNull User user)
            throws DBException, CommandParamException {
        final Map<String, String> paramMap = getRequiredParams(UserParams.ID, UserParams.STATUS);
        final UserService userService = serviceFactory.getUserService();
        final long userId = CommandUtil.parseLongParam(paramMap.get(UserParams.ID));
        final User.Status newStatus = CommandUtil.parseUserStatusParam(paramMap.get(UserParams.STATUS));

        final CommandResult commandResult = CommandResultImpl.of(Paths.USERS_MANAGEMENT_PAGE);
        boolean statusUpdated = false;
        try {
            statusUpdated = userService.updateUserStatus(userId, newStatus);
        } catch (NoSuchElementException ex) {
            logger.warn("Failed to update user status! User does not exist! User id: {}", userId);
        }
        if (statusUpdated) {
            logger.info("User status changed: user id: {}, new status: {}", userId, newStatus);
            commandResult.addMessage(CommandResult.MessageType.SUCCESS, "User status changed successfully");
        } else {
            logger.warn("Failed to change user status: user id: {}, new status: {}", userId, newStatus);
            commandResult.addMessage(CommandResult.MessageType.FAIL, "Failed to change user status");
        }
        return commandResult;
    }
}
