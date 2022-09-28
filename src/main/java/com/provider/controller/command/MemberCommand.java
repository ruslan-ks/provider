package com.provider.controller.command;

import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import com.provider.service.UserService;
import com.provider.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Command that may be accessed only by legal signed-in users with any role >= member
 */
public abstract class MemberCommand extends UserAccessCommand {
    private static final Logger logger = LoggerFactory.getLogger(MemberCommand.class);

    protected MemberCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected boolean hasAccessRights(@NotNull User user) throws DBException {
        final UserService userService = UserServiceImpl.newInstance();
        final Optional<User> foundUser = userService.findUserById(user.getId());
        return foundUser.isPresent() && userService.isActiveUser(foundUser.get());
    }

    //TODO: override executeDenied and give user a message
}
