package com.provider.functions;

import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import com.provider.service.ServiceFactory;
import com.provider.service.ServiceFactoryImpl;
import com.provider.service.UserService;
import org.jetbrains.annotations.NotNull;

/**
 * Used as EL functions configured in functions.tld for jsp user role checking
 */
public class RoleCheckingFunctions {
    private static final ServiceFactory serviceFactory = ServiceFactoryImpl.newInstance();
    private RoleCheckingFunctions() {}

    public static boolean hasAdminRights(@NotNull User user) {
        try {
            final UserService userService = serviceFactory.getUserService();
            return userService.hasAdminRights(user);
        } catch (DBException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static boolean hasRootRights(@NotNull User user) {
        try {
            final UserService userService = serviceFactory.getUserService();
            return userService.hasRootRights(user);
        } catch (DBException ex) {
            throw new RuntimeException(ex);
        }
    }
}
