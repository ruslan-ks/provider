package com.provider.functions;

import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import com.provider.service.UserService;
import com.provider.service.UserServiceImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Used as EL functions configured in functions.tld for jsp user role checking
 */
public class RoleCheckingFunctions {
    private RoleCheckingFunctions() {}

    public static boolean hasAdminRights(@NotNull User user) {
        try {
            final UserService userService = UserServiceImpl.newInstance();
            return userService.hasAdminRights(user);
        } catch (DBException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static boolean hasRootRights(@NotNull User user) {
        try {
            final UserService userService = UserServiceImpl.newInstance();
            return userService.hasRootRights(user);
        } catch (DBException ex) {
            throw new RuntimeException(ex);
        }
    }
}
