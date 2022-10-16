package com.provider.controller.command.impl;

import com.provider.constants.Messages;
import com.provider.constants.Paths;
import com.provider.constants.attributes.SessionAttributes;
import com.provider.constants.params.SignInParams;
import com.provider.controller.command.FrontCommand;
import com.provider.controller.command.result.CommandResult;
import com.provider.controller.command.result.CommandResultImpl;
import com.provider.service.UserService;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class SignInCommand extends FrontCommand {
    SignInCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    public @NotNull CommandResult execute() throws ServletException, IOException, DBException, CommandParamException {
        final Map<String, String> paramMap = getRequestParams(SignInParams.LOGIN, SignInParams.PASSWORD);
        final String login = paramMap.get(SignInParams.LOGIN);
        final String password = paramMap.get(SignInParams.PASSWORD);

        final UserService userService = serviceFactory.getUserService();
        final Optional<User> userOptional = userService.authenticate(login, password);
        final Optional<HttpSession> sessionOptional = getSession();

        final CommandResult failedCommandResult = CommandResultImpl.of(Paths.SIGN_IN_JSP);
        final String userFailMessage;
        if (userOptional.isEmpty()) {
            // invalid login or password
            userFailMessage = Messages.INVALID_LOGIN_OR_PASS;
        } else if (!userService.isActiveUser(userOptional.get())) {
            // user is not active - he was suspended, and he's not allowed to sign in
            userFailMessage =  Messages.YOU_WERE_SUSPENDED;
        } else if (sessionOptional.isEmpty()) {
            // there is no session, we just cannot save this guy
            userFailMessage = Messages.SESSIONS_NOT_ALLOWED;
        } else {
            // success
            sessionOptional.get().setAttribute(SessionAttributes.SIGNED_USER, userOptional.get());
            return CommandResultImpl.of(Paths.USER_PANEL_PAGE);
        }
        failedCommandResult.addMessage(CommandResult.MessageType.FAIL, userFailMessage);
        return failedCommandResult;
    }
}
