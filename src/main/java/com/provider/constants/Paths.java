package com.provider.constants;

import com.provider.constants.params.CommandParams;

/**
 * Resource paths relative to the project root.
 * Suffixes meaning:
 *      _JSP - jsp page location
 *      _PAGE - page access through servlet
 *      no suffix - action handler
 */
public class Paths {
    private Paths() {}

    /**
     * Sign in JSP page
     */
    public static final String SIGN_IN_JSP = "signIn";

    /**
     * User panel page - hidden under /WEB-INF/
     */
    public static final String USER_PANEL_JSP = "/WEB-INF/pages/userPanel.jsp";

    /**
     * Replenish account - hidden under /WEB-INF/
     */
    public static final String REPLENISH_JSP = "/WEB-INF/pages/replenish.jsp";

    /**
     * User management jsp
     */
    public static final String USERS_MANAGEMENT_JSP = "/WEB-INF/pages/usersManagement.jsp";

    /**
     * Front controller servlet
     */
    private static final String CONTROLLER = "controller";

    /**
     * Sign in controller action
     */
    public static final String SIGN_IN = CONTROLLER + "?" + CommandParams.COMMAND + "=" + CommandParams.SIGN_IN;

    /**
     * Sign out controller action
     */
    public static final String SIGN_OUT = CONTROLLER + "?" + CommandParams.COMMAND + "=" + CommandParams.SIGN_OUT;

    /**
     *
     */
    public static final String REPLENISH = CONTROLLER + "?" + CommandParams.COMMAND + "=" + CommandParams.REPLENISH;

    /**
     * User panel controller action
     */
    public static final String USER_PANEL_PAGE = CONTROLLER + "?" + CommandParams.COMMAND + "=" +
            CommandParams.USER_PANEL;

    /**
     * Replenish page - allows to replenish user account
     */
    public static final String REPLENISH_PAGE = CONTROLLER + "?" + CommandParams.COMMAND + "=" +
            CommandParams.REPLENISH_PAGE;

    /**
     * User management page access
     */
    public static final String USERS_MANAGEMENT_PAGE = CONTROLLER + "?" + CommandParams.COMMAND + "=" +
            CommandParams.USERS_MANAGEMENT_PAGE;

    /**
     * Update user action handler
     */
    public static final String UPDATE_USER = CONTROLLER + "?" + CommandParams.COMMAND + "=" +
            CommandParams.UPDATE_USER;

    public static final String ADD_USER = CONTROLLER + "?" + CommandParams.COMMAND + "=" +
            CommandParams.ADD_USER;
}
