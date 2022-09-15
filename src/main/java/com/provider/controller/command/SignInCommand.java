package com.provider.controller.command;

import com.provider.constants.attributes.SessionAttributes;
import com.provider.constants.params.SignInParams;
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

public class SignInCommand implements FrontCommand {
    private static final Logger logger = LoggerFactory.getLogger(SignInCommand.class);

    private final String paramLogin;
    private final String paramPassword;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    private SignInCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response)
            throws CommandParamException {
        final String paramLogin = request.getParameter(SignInParams.LOGIN);
        final String paramPassword = request.getParameter(SignInParams.PASSWORD);
        if (paramLogin == null || paramPassword == null) {
            throw new CommandParamException("Wrong request parameters");
        }
        this.paramLogin = paramLogin;
        this.paramPassword = paramPassword;
        this.request = request;
        this.response = response;
    }

    public static @NotNull FrontCommand newInstance(@NotNull HttpServletRequest request,
                                                    @NotNull HttpServletResponse response)
            throws CommandParamException {
        return new SignInCommand(request, response);
    }

    @Override
    public void execute() throws ServletException, IOException, DBException {
        // TODO: remove this logs
        logger.debug("SignInCommand.execute: login: {}, password: {}", paramLogin, paramPassword);

        final UserService userService = UserServiceImpl.newInstance();
        final Optional<User> userOptional = userService.authenticate(paramLogin, paramPassword);
        if (userOptional.isPresent()) {
            final HttpSession session = request.getSession();
            session.setAttribute(SessionAttributes.SIGNED_USER, userOptional.get());
            response.sendRedirect("userPanel");
            return;
        }
        response.sendRedirect("signIn?" + SignInParams.FAILED_TO_SIGN_IN + "=true");
    }

    private boolean isSomeoneSignedIn() {
        return request.getSession().getAttribute(SessionAttributes.SIGNED_USER) != null;
    }
}
