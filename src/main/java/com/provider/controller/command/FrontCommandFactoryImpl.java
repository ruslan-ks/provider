package com.provider.controller.command;

import com.provider.constants.params.CommandParams;
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
                return SignInCommand.newInstance(request, response);
            default:
                throw new IllegalCommandException("Unknown command: " + paramCommand);
        }
    }
}
