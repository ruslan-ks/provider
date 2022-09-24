package com.provider.controller.command;

import com.provider.constants.attributes.SessionAttributes;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
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
     * Sign in command
     */
    public static final String SIGN_IN = "signIn";

    /**
     * Sign out command
     */
    public static final String SIGN_OUT = "signOut";

    /**
     * Get user panel page command
     */
    public static final String USER_PANEL = "userPanel";

    /**
     * Get replenish page command
     */
    public static final String REPLENISH_PAGE = "replenishPage";

    /**
     * Replenish account command
     */
    public static final String REPLENISH = "replenish";

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
    public abstract void execute() throws DBException, ServletException, IOException, CommandParamException;

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
    protected final @NotNull Optional<User> getSignedInUser() {
        return getSession().map(s -> (User) s.getAttribute(SessionAttributes.SIGNED_USER));
    }
}
