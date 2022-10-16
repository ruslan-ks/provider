package com.provider.controller.command.impl;

import com.provider.constants.Messages;
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
import com.provider.service.exception.InvalidPropertyException;
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
    protected @NotNull CommandResult executeAccessed(@NotNull User user)
            throws DBException, CommandParamException {
        final Map<String, String> params = getRequestParams(UserParams.LOGIN, UserParams.PASSWORD,
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

        boolean userInserted = false;
        try {
            userInserted = userService.insertUser(newUser, password);
        } catch (InvalidPropertyException ex) {
            throw new CommandParamException(ex);
        } catch (DBException ex) {
            // TODO: add unique constraint violation handling
            logger.error("DBException", ex);
        }

        final CommandResult commandResult = CommandResultImpl.of(Paths.USERS_MANAGEMENT_PAGE);
        if (userInserted) {
            commandResult.addMessage(CommandResult.MessageType.SUCCESS, Messages.USER_INSERT_SUCCESS);
        } else {
            commandResult.addMessage(CommandResult.MessageType.FAIL, Messages.USER_INSERT_FAIL);
        }
        return commandResult;
    }
}
