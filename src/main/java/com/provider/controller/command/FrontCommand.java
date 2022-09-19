package com.provider.controller.command;

import com.provider.dao.exception.DBException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

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
     * Constants - page paths
     */
    final protected String userPanelPath;
    final protected String signInPath;

    protected FrontCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        this.request = request;
        this.response = response;

        final ServletContext context = request.getServletContext();
        userPanelPath = request.getContextPath() + "/" + context.getInitParameter("userPanel");
        signInPath = request.getContextPath() + "/" + context.getInitParameter("signIn");
    }

    /**
     * Executes user's request. Effects request and response objects(if provided).
     * Returns result via calling forward or redirect.
     */
    public abstract void execute() throws DBException, ServletException, IOException;
}
