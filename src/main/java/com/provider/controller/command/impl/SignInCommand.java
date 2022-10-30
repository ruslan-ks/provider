package com.provider.controller.command.impl;

import com.provider.constants.Messages;
import com.provider.constants.Paths;
import com.provider.constants.attributes.SessionAttributes;
import com.provider.constants.params.SignInParams;
import com.provider.controller.command.FrontCommand;
import com.provider.controller.command.result.CommandResult;
import com.provider.service.UserService;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class SignInCommand extends FrontCommand {
    private static final Logger logger = LoggerFactory.getLogger(SignInCommand.class);

    SignInCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    public @NotNull CommandResult execute() throws ServletException, IOException, DBException, CommandParamException {
        final Map<String, String> paramMap = getRequiredParams(SignInParams.LOGIN, SignInParams.PASSWORD);
        final String login = paramMap.get(SignInParams.LOGIN);
        final String password = paramMap.get(SignInParams.PASSWORD);

        final UserService userService = serviceFactory.getUserService();
        final Optional<User> userOptional = userService.authenticate(login, password);

        final CommandResult failedCommandResult = newCommandResult(Paths.SIGN_IN_JSP);
        if (userOptional.isEmpty()) {
            // Invalid login or password
            logger.warn("Authentication failed: Invalid login or password: login: {}", login);
            return failedCommandResult.addMessage(CommandResult.MessageType.FAIL, Messages.INVALID_LOGIN_OR_PASS);
        } else if (!userService.isActiveUser(userOptional.get())) {
            // User is not active - he was suspended, he's not allowed to sign in
            logger.warn("Authentication failed: user is SUSPENDED: user: {}", userOptional.get());
            return failedCommandResult.addMessage(CommandResult.MessageType.FAIL, Messages.YOU_WERE_SUSPENDED);
        } else {
            // Success
            getSession().setAttribute(SessionAttributes.SIGNED_USER, userOptional.get());
            logger.debug("User authenticated: {}", userOptional.get());
            return newCommandResult(Paths.USER_PANEL_PAGE);
        }
    }
}
