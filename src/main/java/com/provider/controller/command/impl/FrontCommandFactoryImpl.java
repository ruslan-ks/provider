package com.provider.controller.command.impl;

import com.provider.constants.Paths;
import com.provider.controller.command.FrontCommand;
import com.provider.controller.command.FrontCommandFactory;
import com.provider.controller.command.exception.CommandAccessException;
import com.provider.controller.command.exception.CommandParamException;
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
            throws IllegalCommandException, CommandAccessException, CommandParamException {
        final String paramCommand = request.getParameter(Paths.COMMAND);
        if (paramCommand == null) {
            throw new IllegalCommandException("Parameter '" + Paths.COMMAND + "' is null");
        }
        switch (paramCommand) {
            case FrontCommand.SIGN_IN:
                return new SignInCommand(request, response);
            case FrontCommand.SIGN_OUT:
                return new SignOutCommand(request, response);
            case FrontCommand.USER_PANEL:
                return new UserPanelPageCommand(request, response);
            case FrontCommand.REPLENISH_PAGE:
                return new ReplenishPageCommand(request, response);
            case FrontCommand.REPLENISH:
                return new ReplenishCommand(request, response);
            default:
                throw new IllegalCommandException("Unknown command: " + paramCommand);
        }
    }
}
