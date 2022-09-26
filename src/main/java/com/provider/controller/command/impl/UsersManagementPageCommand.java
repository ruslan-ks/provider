package com.provider.controller.command.impl;

import com.provider.constants.Paths;
import com.provider.controller.command.AdminCommand;
import com.provider.controller.command.result.CommandResult;
import com.provider.controller.command.result.CommandResultImpl;
import com.provider.entity.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

public class UsersManagementPageCommand extends AdminCommand {
    public UsersManagementPageCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected CommandResult executeAccessed(@NotNull User user) {
        return CommandResultImpl.of(Paths.USERS_MANAGEMENT_JSP);
    }
}
