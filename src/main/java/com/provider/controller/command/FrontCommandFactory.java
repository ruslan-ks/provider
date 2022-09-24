package com.provider.controller.command;

import com.provider.controller.command.exception.CommandAccessException;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.exception.FrontCommandException;
import com.provider.controller.command.exception.IllegalCommandException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

/**
 * Is used by front controller servlet to receive appropriate command.
 */
public interface FrontCommandFactory {
    /**
     * Returns appropriate command required by request parameters. May create new command or return cached one.
     * @param request doGet/doPost request parameter
     * @param response doGet/doPost response parameter
     * @param config calling servlet config
     * @return appropriate command according to the request parameters
     */
    @NotNull FrontCommand getCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                     @NotNull ServletConfig config)
            throws IllegalCommandException, CommandAccessException, CommandParamException;
}
