package com.provider.controller.command;

import com.provider.constants.attributes.SessionAttributes;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import com.provider.service.ServiceFactory;
import com.provider.service.ServiceFactoryImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Command used by the front controller servlet.
 */
public abstract class FrontCommand {

    /**
     * Incoming request
     */
    protected final HttpServletRequest request;

    /**
     * Resulting response
     */
    protected final HttpServletResponse response;

    /**
     * Service factory that should be used to obtain service objects
     */
    protected ServiceFactory serviceFactory = ServiceFactoryImpl.newInstance();

    protected FrontCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    /**
     * Executes user's request. Effects request and response objects(if provided).
     * Returns result via calling forward or redirect.
     * @return CommandResult - object containing result data - page location for redirect/forward and so on
     */
    public abstract CommandResult execute() throws DBException, ServletException, IOException, CommandParamException;

    /**
     * Returns current session
     * @return current session
     */
    protected final @NotNull Optional<HttpSession> getSession() {
        return Optional.ofNullable(request.getSession());
    }

    /**
     * Returns signed-in user
     * @return Optional containing signed-in user if session is present and user attribute is saved to the session
     */
    protected final @NotNull Optional<User> getSessionUser() {
        return getSession().map(s -> (User) s.getAttribute(SessionAttributes.SIGNED_USER));
    }

    /**
     * Extracts and returns request parameters, works only fo parameters of type String(not for String[])
     *
     * @param requiredParamNames required parameter names
     * @return Immutable Map containing parameter name -> value[] pairs; may contain more params than required
     * @throws CommandParamException if at least one of required parameters not found
     **/
    protected final @NotNull Map<String, String> getRequestParams(String... requiredParamNames)
            throws CommandParamException {
        final Map<String, String> paramMap = request.getParameterMap().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue()[0]));
        for (var name : requiredParamNames) {
            if (!paramMap.containsKey(name)) {
                throw new CommandParamException("Parameter '" + name + "' is null");
            }
        }
        return paramMap;
    }

    // TODO: delete or refactor
    protected final @NotNull Map<String, Optional<String>> getOptionalRequestParams() {
        return Collections.list(request.getParameterNames()).stream()
                .collect(Collectors.toMap(Function.identity(), request::getParameter))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> Optional.ofNullable(e.getValue())));

        /*
        return request.getParameterMap().entrySet().stream()
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), Optional.ofNullable(e.getValue())))
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue().map(arr -> arr[0])))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
         */
    }
}
