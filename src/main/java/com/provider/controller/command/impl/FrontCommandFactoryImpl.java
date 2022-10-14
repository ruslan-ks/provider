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
        return switch (paramCommand) {
            case CommandParams.SIGN_IN -> new SignInCommand(request, response);
            case CommandParams.SIGN_OUT -> new SignOutCommand(request, response);
            case CommandParams.USER_PANEL -> new UserPanelPageCommand(request, response);
            case CommandParams.REPLENISH_PAGE -> new ReplenishPageCommand(request, response);
            case CommandParams.REPLENISH -> new ReplenishCommand(request, response);
            case CommandParams.USERS_MANAGEMENT_PAGE -> new UsersManagementPageCommand(request, response);
            case CommandParams.ADD_USER -> new AddUserCommand(request, response);
            case CommandParams.UPDATE_USER_STATUS -> new UpdateUserStatusCommand(request, response);
            case CommandParams.TARIFFS_MANAGEMENT_PAGE -> new TariffsManagementPageCommand(request, response);
            case CommandParams.ADD_SERVICE -> new AddServiceCommand(request, response);
            default -> throw new IllegalCommandException("Unknown command: " + paramCommand);
        };
    }
}
