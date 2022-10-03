package com.provider.functions;

import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import com.provider.service.ServiceFactory;
import com.provider.service.ServiceFactoryImpl;
import com.provider.service.UserService;

import java.util.Arrays;

public class EnumDataFunctions {
    private EnumDataFunctions() {}

    private static final ServiceFactory serviceFactory = ServiceFactoryImpl.newInstance();

    public static Iterable<User.Status> allUserStatuses() {
        return Arrays.asList(User.Status.values());
    }

    /**
     * Returns iterable of roles that may be created by the user
     * @param user user trying to create another user
     * @return iterable of roles that may be created by the user
     */
    public static Iterable<User.Role> rolesAllowedForCreation(User user) {
        try {
            final UserService userService = serviceFactory.getUserService();
            return userService.rolesAllowedForCreation(user);
        } catch (DBException ex) {
            throw new RuntimeException();
        }
    }
}
