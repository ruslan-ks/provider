package com.provider.controller.command;

import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Optional;

/**
 * Checks if user is allowed to execute a command before executing
 */
public abstract class UserAccessCommand extends FrontCommand {
    public UserAccessCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    public final @NotNull CommandResult execute()
            throws DBException, ServletException, IOException, CommandParamException {
        final Optional<User> user = getSessionUser();
        if (user.isPresent() && hasAccessRights(user.get())) {
            return executeAuthorized(user.get());
        }
        return executeDenied();
    }

    /**
     * Defines if a command may be executed by the useR
     * @param user signed-in user
     * @return true if command may be executed by the user, false otherwise
     */
    protected abstract boolean hasAccessRights(@NotNull User user) throws DBException;

    /**
     * Executed if a resource can be accessed by a user.
     * Must never be called from outside this class.
     * Guarantees that <code>getSignedInUser()</code> returns not empty optional when called from inside this method
     * @param user signed-in user guaranteed to be existing user
     */
    protected abstract @NotNull CommandResult executeAuthorized(@NotNull User user)
            throws DBException, ServletException, IOException, CommandParamException;

    /**
     * Called if access is denied.
     * Sends redirect to the sign-in page by default
     */
    protected abstract @NotNull CommandResult executeDenied();
}
