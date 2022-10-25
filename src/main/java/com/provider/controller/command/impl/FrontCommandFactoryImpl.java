package com.provider.controller.command.impl;

import com.provider.constants.params.CommandParams;
import com.provider.controller.command.FrontCommand;
import com.provider.controller.command.FrontCommandFactory;
import com.provider.controller.command.exception.IllegalCommandException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.provider.constants.params.CommandParams.*;

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

    private static final Map<String, Class<? extends FrontCommand>> commandClassMap;

    static {
        final Map<String, Class<? extends FrontCommand>> mutableTmpMap = new HashMap<>(Map.of(
                SIGN_IN, SignInCommand.class,
                SIGN_OUT, SignOutCommand.class,
                USER_PANEL, UserPanelPageCommand.class,
                REPLENISH_PAGE, ReplenishPageCommand.class,
                REPLENISH, ReplenishCommand.class,
                USERS_MANAGEMENT_PAGE, UsersManagementPageCommand.class,
                ADD_USER, AddUserCommand.class,
                UPDATE_USER_STATUS, UpdateUserStatusCommand.class,
                TARIFFS_MANAGEMENT_PAGE, TariffsManagementPageCommand.class,
                ADD_SERVICE, AddServiceCommand.class
        ));
        mutableTmpMap.put(ADD_TARIFF, AddTariffCommand.class);
        mutableTmpMap.put(CATALOG_PAGE, CatalogPageCommand.class);
        mutableTmpMap.put(SUBSCRIBE, SubscribeCommand.class);
        mutableTmpMap.put(UNSUBSCRIBE, UnsubscribeCommand.class);

        commandClassMap = Collections.unmodifiableMap(mutableTmpMap);
    }

    @Override
    public @NotNull FrontCommand getCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                            @NotNull ServletConfig config)
            throws IllegalCommandException {
        final String commandParam = request.getParameter(CommandParams.COMMAND);
        if (commandParam == null) {
            throw new IllegalCommandException("Parameter '" + CommandParams.COMMAND + "' is null");
        }

        final Optional<Class<? extends FrontCommand>> foundCommandClass =
                Optional.ofNullable(commandClassMap.get(commandParam));
        if (foundCommandClass.isEmpty()) {
            throw new IllegalCommandException("Unknown command: " + commandParam);
        }
        try {
            return foundCommandClass.get()
                    .getDeclaredConstructor(HttpServletRequest.class, HttpServletResponse.class)
                    .newInstance(request, response);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
