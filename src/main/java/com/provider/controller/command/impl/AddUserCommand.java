package com.provider.controller.command.impl;

import com.provider.constants.Messages;
import com.provider.constants.Paths;
import com.provider.constants.params.UserParams;
import com.provider.controller.command.AdminCommand;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.controller.command.result.CommandResultImpl;
import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import com.provider.entity.user.impl.UserImpl;
import com.provider.service.UserService;
import com.provider.service.exception.InvalidPropertyException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class AddUserCommand extends AdminCommand {
    AddUserCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected CommandResult executeAccessed(@NotNull User user)
            throws DBException, CommandParamException {
        final Map<String, String> params = getRequestParams(UserParams.LOGIN, UserParams.PASSWORD,
                UserParams.NAME, UserParams.SURNAME, UserParams.PHONE);

        final UserService userService = serviceFactory.getUserService();
        final String login = params.get(UserParams.LOGIN);
        final String password = params.get(UserParams.PASSWORD);
        final String name = params.get(UserParams.NAME);
        final String surname = params.get(UserParams.SURNAME);
        final String phone = params.get(UserParams.PHONE);
        final User newUser = UserImpl.of(0, name, surname, login, phone, User.Role.MEMBER, User.Status.ACTIVE);

        final boolean userInserted;
        try {
            userInserted = userService.insertUser(newUser, password);
        } catch (InvalidPropertyException ex) {
            throw new CommandParamException(ex);
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
