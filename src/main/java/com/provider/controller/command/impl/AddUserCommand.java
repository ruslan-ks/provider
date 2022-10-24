package com.provider.controller.command.impl;

import com.provider.constants.Messages;
import com.provider.constants.Paths;
import com.provider.constants.params.UserParams;
import com.provider.controller.command.AdminCommand;
import com.provider.controller.command.CommandUtil;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import com.provider.service.UserService;
import com.provider.service.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class AddUserCommand extends AdminCommand {
    private static final Logger logger = LoggerFactory.getLogger(AddUserCommand.class);

    AddUserCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected @NotNull CommandResult executeAuthorized(@NotNull User user)
            throws DBException, CommandParamException {
        final Map<String, String> params = getRequiredParams(UserParams.LOGIN, UserParams.PASSWORD,
                UserParams.NAME, UserParams.SURNAME, UserParams.PHONE, UserParams.ROLE);

        final UserService userService = serviceFactory.getUserService();
        final String login = params.get(UserParams.LOGIN);
        final String password = params.get(UserParams.PASSWORD);
        final String name = params.get(UserParams.NAME);
        final String surname = params.get(UserParams.SURNAME);
        final String phone = params.get(UserParams.PHONE);
        final User.Role role = CommandUtil.parseUserRoleParam(params.get(UserParams.ROLE));

        if (!userService.rolesAllowedForCreation(user).contains(role)) {
            throw new CommandParamException();
        }
        final User newUser = entityFactory.newUser(0, name, surname, login, phone, role, User.Status.ACTIVE);

        final CommandResult commandResult = newCommandResult(Paths.USERS_MANAGEMENT_PAGE);
        boolean userInserted = false;
        try {
            userInserted = userService.insertUser(newUser, password);
        } catch (ValidationException ex) {
            logger.warn("Failed to insert user: invalid user: {}", user);
            logger.warn("Failed to insert user", ex);
            commandResult.addMessage(CommandResult.MessageType.FAIL, Messages.INVALID_USER_PARAMS);
        }
        if (userInserted) {
            return commandResult.addMessage(CommandResult.MessageType.SUCCESS, Messages.USER_INSERT_SUCCESS);
        }
        return commandResult.addMessage(CommandResult.MessageType.FAIL, Messages.USER_INSERT_FAIL);
    }
}
