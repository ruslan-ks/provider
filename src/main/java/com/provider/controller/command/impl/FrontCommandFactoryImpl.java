package com.provider.controller.command.impl;

import com.provider.constants.params.CommandParams;
import com.provider.controller.command.FrontCommand;
import com.provider.controller.command.FrontCommandFactory;
import com.provider.controller.command.exception.FrontCommandException;
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
    public @NotNull FrontCommand getCommand(@NotNull HttpServletRequest request,
                                            @NotNull HttpServletResponse response,
                                            @NotNull ServletConfig config) throws FrontCommandException {
        final String paramCommand = request.getParameter(CommandParams.COMMAND);
        if (paramCommand == null) {
            throw new IllegalCommandException("Parameter '" + CommandParams.COMMAND + "' is null");
        }
        switch (paramCommand) {
            case CommandParams.SIGN_IN:
                return new SignInCommand(request, response);
            case CommandParams.SIGN_OUT:
                return new SignOutCommand(request, response);
            default:
                throw new IllegalCommandException("Unknown command: " + paramCommand);
        }
    }
}
