package com.provider.controller.command.impl;

import com.provider.constants.Paths;
import com.provider.constants.attributes.SessionAttributes;
import com.provider.controller.command.MemberAccessCommand;
import com.provider.controller.command.result.CommandResult;
import com.provider.controller.command.result.CommandResultImpl;
import com.provider.entity.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

public class SignOutCommand extends MemberAccessCommand {
    SignOutCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected CommandResult executeAccessed(@NotNull User user) {
        getSession().ifPresent(s -> s.removeAttribute(SessionAttributes.SIGNED_USER));
        return CommandResultImpl.of(Paths.SIGN_IN_JSP);
    }
}
