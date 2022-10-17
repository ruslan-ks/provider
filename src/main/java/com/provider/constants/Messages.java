package com.provider.constants;

/**
 * Messages shown to a user
 */
// TODO: add messages localization
public class Messages {
    private Messages() {}

    public static final String USER_INSERT_SUCCESS = "User successfully inserted";

    public static final String USER_INSERT_FAIL = "Failed to insert user";

    public static final String YOU_WERE_SUSPENDED = "You were suspended by an administrator";

    public static final String INVALID_LOGIN_OR_PASS = "Invalid login or password";

    public static final String SESSIONS_NOT_ALLOWED = "Sessions are not allowed. " +
            "Please turn on the cookies and try again";

    public static final String SERVICE_INSERT_SUCCESS = "Service inserted successfully";

    public static final String SERVICE_INSERT_FAIL = "Failed to insert service";

    public static final String INVALID_SERVICE_PARAMS = "Invalid service parameters";

    public static final String TARIFF_INSERT_SUCCESS = "Tariff inserted successfully";

    public static final String TARIFF_INSERT_FAIL = "Failed to insert tariff";

    public static final String INVALID_TARIFF_PARAMS = "Invalid tariff parameters";
}
