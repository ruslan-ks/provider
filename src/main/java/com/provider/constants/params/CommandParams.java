package com.provider.constants.params;

/**
 * Constant class. Contains command names
 * Used when specifying action attribute, to get smt like: <code>action="<servlet mapping>?command=signIn</code>
 */
public final class CommandParams {
    private CommandParams() {}

    public static final String COMMAND = "command";
    public static final String SIGN_IN = "signIn";
    public static final String SIGN_OUT = "signOut";
    public static final String USER_PANEL = "userPanel";
}
