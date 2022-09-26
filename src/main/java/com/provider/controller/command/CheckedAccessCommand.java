package com.provider.controller.command;

import com.provider.constants.Paths;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.controller.command.result.CommandResultImpl;
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
public abstract class CheckedAccessCommand extends FrontCommand {
    public CheckedAccessCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    public final CommandResult execute() throws DBException, ServletException, IOException, CommandParamException {
        final Optional<User> user = getSessionUser();
        if (user.isPresent() && hasAccessRights(user.get())) {
            return executeAccessed(user.get());
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
    protected abstract CommandResult executeAccessed(@NotNull User user)
            throws DBException, ServletException, IOException, CommandParamException;

    /**
     * Called if access is denied.
     * Sends redirect to the sign-in page by default
     */
    protected CommandResult executeDenied() {
        // TODO: add message parameter, so signIn.jsp could show it
        return CommandResultImpl.of(Paths.SIGN_IN_JSP);
    }
}
