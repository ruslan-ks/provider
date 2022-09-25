package com.provider.controller.command.impl;

import com.provider.constants.Paths;
import com.provider.constants.attributes.SessionAttributes;
import com.provider.constants.params.SignInParams;
import com.provider.controller.command.FrontCommand;
import com.provider.controller.command.result.CommandResult;
import com.provider.controller.command.result.CommandResultImpl;
import com.provider.service.UserService;
import com.provider.service.UserServiceImpl;
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
    public CommandResult execute() throws ServletException, IOException, DBException, CommandParamException {
        final Map<String, String> paramMap = getRequestParams(SignInParams.LOGIN, SignInParams.PASSWORD);
        final String login = paramMap.get(SignInParams.LOGIN);
        final String password = paramMap.get(SignInParams.PASSWORD);

        final UserService userService = UserServiceImpl.newInstance();
        final Optional<User> userOptional = userService.authenticate(login, password);
        final Optional<HttpSession> sessionOptional = getSession();
        if (userOptional.isPresent() && sessionOptional.isPresent()) {
            sessionOptional.get().setAttribute(SessionAttributes.SIGNED_USER, userOptional.get());
            return CommandResultImpl.of(Paths.USER_PANEL_PAGE);
        }
        return CommandResultImpl.of(Paths.SIGN_IN_JSP + "?" + SignInParams.FAILED_TO_SIGN_IN + "=true");
    }
}
