package com.provider.controller.command;

import com.provider.constants.Paths;
import com.provider.constants.attributes.SessionAttributes;
import com.provider.constants.params.SignInMessageParams;
import com.provider.controller.command.result.CommandResult;
import com.provider.controller.command.result.CommandResultImpl;
import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import com.provider.service.UserService;
import com.provider.service.UserServiceImpl;
import com.provider.util.ParameterizedUrl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * Command that may be accessed only by legal signed-in users with any role >= member
 */
public abstract class MemberCommand extends UserAccessCommand {
    private static final Logger logger = LoggerFactory.getLogger(MemberCommand.class);

    private boolean isMember;
    private boolean isActive;

    protected MemberCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected boolean hasAccessRights(@NotNull User user) throws DBException {
        final UserService userService = UserServiceImpl.newInstance();
        final Optional<User> foundUser = userService.findUserById(user.getId());
        isMember = foundUser.isPresent();
        if (isMember) {
            isActive = userService.isActiveUser(foundUser.get());
        }
        return isMember && isActive;
    }

    @Override
    protected CommandResult executeDenied() {
        if (isMember) {
            getSession().orElseThrow().removeAttribute(SessionAttributes.SIGNED_USER);
            final Map<String, String> messageParams = isActive
                    ? Collections.emptyMap()
                    : Map.of(SignInMessageParams.SUSPENDED, "true");
            final ParameterizedUrl url = ParameterizedUrl.of(Paths.SIGN_IN_JSP, messageParams);
            return CommandResultImpl.of(url.getString());
        }
        return CommandResultImpl.of(Paths.SIGN_IN_JSP);
    }
}
