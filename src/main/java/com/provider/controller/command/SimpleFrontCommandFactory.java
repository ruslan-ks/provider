package com.provider.controller.command;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

/**
 * FrontCommand factory implementation.
 */
public class SimpleFrontCommandFactory implements FrontCommandFactory {

    /**
     * A convenience method.
     * @return new factory instance
     */
    public static SimpleFrontCommandFactory newInstance() {
        return new SimpleFrontCommandFactory();
    }

    @Override
    public @NotNull FrontCommand getCommand(@NotNull HttpServletRequest request,
                                            @NotNull HttpServletResponse response,
                                            @NotNull ServletConfig config) {
        throw new UnsupportedOperationException();
    }
}
