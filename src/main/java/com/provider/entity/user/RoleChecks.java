package com.provider.entity.user;

import org.jetbrains.annotations.NotNull;

/**
 * Used as EL functions configured in functions.tld for jsp user role checking
 */
public class RoleChecks {
    private RoleChecks() {}

    public static boolean isAdminOrHigher(@NotNull User user) {
        return user.getRole().equals(User.Role.ADMIN) || user.getRole().equals(User.Role.ROOT);
    }

    public static boolean isRoot(@NotNull User user) {
        return user.getRole().equals(User.Role.ROOT);
    }
}
