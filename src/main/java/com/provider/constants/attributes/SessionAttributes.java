package com.provider.constants.attributes;

/**
 * Constant class containing session attribute names
 */
public class SessionAttributes {
    private SessionAttributes() {}

    /**
     * Attribute of type {@link com.provider.entity.settings.UserSettings}
     */
    public static final String USER_SETTINGS = "userSettings";

    /**
     * Attribute of type {@link com.provider.entity.user.User}
     */
    public static final String SIGNED_USER = "signedUser";

    /**
     * Attribute of type {@code Queue<Pair<CommandResult.MessageType, String>>} - collection of messages
     */
    public static final String MESSAGES = "messages";
}
