package com.provider.constants.attributes;

/**
 * Constant class containing request attribute names
 */
public class RequestAttributes {
    private RequestAttributes() {}

    public static final String PAGE_COUNT = "pageCount";

    public static final String USER_ACCOUNTS = "userAccounts";

    /**
     * Attribute of type {@code List<User>}
     */
    public static final String USERS = "users";

    /**
     * Attribute of type {@code List<Service>}
     */
    public static final String SERVICES = "services";

    /**
     * Attribute of type {@code List<TariffDto>}
     */
    public static final String TARIFFS = "tariffs";
}
