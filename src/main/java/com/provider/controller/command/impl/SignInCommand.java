package com.provider.controller.command.impl;

import com.provider.constants.Paths;
import com.provider.constants.attributes.SessionAttributes;
import com.provider.constants.params.SignInParams;
import com.provider.controller.command.FrontCommand;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public class SignInCommand extends FrontCommand {
    private static final Logger logger = LoggerFactory.getLogger(SignInCommand.class);

    private final String paramLogin;
    private final String paramPassword;

    SignInCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response)
            throws CommandParamException {
        super(request, response);
        final String paramLogin = request.getParameter(SignInParams.LOGIN);
        final String paramPassword = request.getParameter(SignInParams.PASSWORD);
        if (paramLogin == null || paramPassword == null) {
            throw new CommandParamException("Wrong request parameters");
        }
        this.paramLogin = paramLogin;
        this.paramPassword = paramPassword;
    }

    @Override
    public void execute() throws ServletException, IOException, DBException {
        logger.debug("SignInCommand.execute: request: {}", request);

        final UserService userService = UserServiceImpl.newInstance();
        final Optional<User> userOptional = userService.authenticate(paramLogin, paramPassword);
        if (userOptional.isPresent()) {
            final HttpSession session = request.getSession();
            session.setAttribute(SessionAttributes.SIGNED_USER, userOptional.get());
            response.sendRedirect(Paths.USER_PANEL);
            return;
        }
        response.sendRedirect(Paths.SIGN_IN_PAGE + "?" + SignInParams.FAILED_TO_SIGN_IN + "=true");
    }
}
