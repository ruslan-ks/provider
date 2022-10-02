package com.provider.functions;

import com.provider.entity.user.User;

import java.util.Arrays;

public class EnumDataFunctions {
    private EnumDataFunctions() {}

    public static Iterable<User.Status> allUserStatuses() {
        return Arrays.asList(User.Status.values());
    }
}
