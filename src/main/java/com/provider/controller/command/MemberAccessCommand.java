package com.provider.controller.command;

import com.provider.constants.Paths;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import com.provider.service.UserService;
import com.provider.service.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Optional;

/**
 * Command that may be accessed only by signed-in users(admins too)
 */
public abstract class MemberAccessCommand extends FrontCommand {
    protected MemberAccessCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    public final void execute() throws DBException, ServletException, IOException, CommandParamException {
        final UserService userService = UserServiceImpl.newInstance();
        final Optional<User> user = getSessionUser();
        if (user.isPresent() && userService.findUserById(user.get().getId()).isPresent()) {
            executeAccessed(user.get());
        } else {
            executeDenied();
        }
    }

    /**
     * Executed if a resource can be accessed by a user.
     * Must never be called from outside this class.
     * Guarantees that <code>getSignedInUser()</code> returns not empty optional when called from inside this method
     * @param user signed-in user guaranteed to be existing user
     */
    protected abstract void executeAccessed(@NotNull User user)
            throws DBException, ServletException, IOException, CommandParamException;

    /**
     * Called if access is denied.
     * Sends redirect to the sign-in page by default
     */
    protected void executeDenied() throws IOException {
        response.sendRedirect(Paths.SIGN_IN_JSP);
    }
}
