package com.provider.controller.command;

import com.provider.dao.exception.DBException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Optional;

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

    protected FrontCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    /**
     * Executes user's request. Effects request and response objects(if provided).
     * Returns result via calling forward or redirect.
     */
    public abstract void execute() throws DBException, ServletException, IOException;

    protected final @NotNull Optional<HttpSession> getSession() {
        return Optional.ofNullable(request.getSession());
    }
}
