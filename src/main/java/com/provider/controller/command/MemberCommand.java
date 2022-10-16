package com.provider.controller.command;

import com.provider.constants.Messages;
import com.provider.constants.Paths;
import com.provider.constants.attributes.SessionAttributes;
import com.provider.controller.command.result.CommandResult;
import com.provider.controller.command.result.CommandResultImpl;
import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import com.provider.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Command that may be accessed only by legal signed-in users with any role >= member
 */
public abstract class MemberCommand extends UserAccessCommand {
    private boolean isMember;
    private boolean isActive;

    protected MemberCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected boolean hasAccessRights(@NotNull User user) throws DBException {
        final UserService userService = serviceFactory.getUserService();
        final Optional<User> foundUser = userService.findUserById(user.getId());
        isMember = foundUser.isPresent();
        if (isMember) {
            isActive = userService.isActiveUser(foundUser.get());
        }
        return isMember && isActive;
    }

    @Override
    protected @NotNull CommandResult executeDenied() {
        if (isMember) {
            getSession().orElseThrow().removeAttribute(SessionAttributes.SIGNED_USER);

            final CommandResult commandResult = CommandResultImpl.of(Paths.SIGN_IN_JSP);
            if (!isActive) {
                commandResult.addMessage(CommandResult.MessageType.FAIL, Messages.YOU_WERE_SUSPENDED);
            }
            return commandResult;
        }
        return CommandResultImpl.of(Paths.SIGN_IN_JSP);
    }
}
