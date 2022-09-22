package com.provider.controller.command.impl;

import com.provider.constants.Paths;
import com.provider.constants.attributes.SessionAttributes;
import com.provider.controller.command.FrontCommand;
import com.provider.dao.exception.DBException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SignOutCommand extends FrontCommand {
    SignOutCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    public void execute() throws DBException, ServletException, IOException {
        final HttpSession session = request.getSession();
        if (session.getAttribute(SessionAttributes.SIGNED_USER) != null) {
            session.removeAttribute(SessionAttributes.SIGNED_USER);
        }
        response.sendRedirect(Paths.SIGN_IN_PAGE);
    }
}
