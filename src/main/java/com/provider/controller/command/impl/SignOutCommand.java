package com.provider.controller.command.impl;

import com.provider.constants.Paths;
import com.provider.constants.attributes.SessionAttributes;
import com.provider.controller.command.MemberAccessCommand;
import com.provider.entity.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SignOutCommand extends MemberAccessCommand {
    SignOutCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected void executeAccessed(@NotNull User user) throws IOException {
        getSession().ifPresent(s -> s.removeAttribute(SessionAttributes.SIGNED_USER));
        response.sendRedirect(Paths.SIGN_IN_JSP);
    }
}
