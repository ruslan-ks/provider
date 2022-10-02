package com.provider.controller.command.impl;

import com.provider.constants.params.CommandParams;
import com.provider.controller.command.FrontCommand;
import com.provider.controller.command.FrontCommandFactory;
import com.provider.controller.command.exception.IllegalCommandException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

/**
 * FrontCommand factory implementation.
 */
public class FrontCommandFactoryImpl implements FrontCommandFactory {

    /**
     * A convenience method.
     * @return new factory instance
     */
    public static FrontCommandFactoryImpl newInstance() {
        return new FrontCommandFactoryImpl();
    }

    @Override
    public @NotNull FrontCommand getCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                            @NotNull ServletConfig config)
            throws IllegalCommandException {
        final String paramCommand = request.getParameter(CommandParams.COMMAND);
        if (paramCommand == null) {
            throw new IllegalCommandException("Parameter '" + CommandParams.COMMAND + "' is null");
        }
        switch (paramCommand) {
            case CommandParams.SIGN_IN:
                return new SignInCommand(request, response);
            case CommandParams.SIGN_OUT:
                return new SignOutCommand(request, response);
            case CommandParams.USER_PANEL:
                return new UserPanelPageCommand(request, response);
            case CommandParams.REPLENISH_PAGE:
                return new ReplenishPageCommand(request, response);
            case CommandParams.REPLENISH:
                return new ReplenishCommand(request, response);
            case CommandParams.USERS_MANAGEMENT_PAGE:
                return new UsersManagementPageCommand(request, response);
            case CommandParams.ADD_USER:
                return new AddUserCommand(request, response);
            default:
                throw new IllegalCommandException("Unknown command: " + paramCommand);
        }
    }
}
