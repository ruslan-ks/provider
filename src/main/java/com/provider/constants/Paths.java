package com.provider.constants;

import com.provider.constants.params.CommandParams;

/**
 * Resource paths relative to the project root.
 * Suffixes meaning:
 *      _JSP - jsp page location
 *      _PAGE - page access through servlet
 *      no suffix - action handler(command)
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
     * Users management jsp
     */
    public static final String USERS_MANAGEMENT_JSP = "/WEB-INF/pages/usersManagement.jsp";

    /**
     * Tariffs management jsp
     */
    public static final String TARIFFS_MANAGEMENT_JSP = "/WEB-INF/pages/tariffsManagement.jsp";

    /**
     * Catalog jsp
     */
    public static final String CATALOG_JSP = "/WEB-INF/pages/catalog.jsp";

    /**
     * Front controller servlet
     */
    public static final String CONTROLLER = "controller";

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
     * Add user action
     */
    public static final String ADD_USER = CONTROLLER + "?" + CommandParams.COMMAND + "=" +
            CommandParams.ADD_USER;

    /**
     * Switch user status action
     */
    public static final String UPDATE_USER_STATUS = CONTROLLER + "?" + CommandParams.COMMAND + "=" +
            CommandParams.UPDATE_USER_STATUS;

    /**
     * Open tariffs management page command
     */
    public static final String TARIFFS_MANAGEMENT_PAGE = CONTROLLER + "?" + CommandParams.COMMAND + "="
            + CommandParams.TARIFFS_MANAGEMENT_PAGE;

    /**
     * Add service command
     */
    public static final String ADD_SERVICE = CONTROLLER + "?" + CommandParams.COMMAND + "="
            + CommandParams.ADD_SERVICE;

    /**
     * Add tariff command
     */
    public static final String ADD_TARIFF = CONTROLLER + "?" + CommandParams.COMMAND + "="
            + CommandParams.ADD_TARIFF;

    /**
     * Catalog page command
     */
    public static final String CATALOG_PAGE = CONTROLLER + "?" + CommandParams.COMMAND + "="
            + CommandParams.CATALOG_PAGE;

    /**
     * Buy tariff action command
     */
    public static final String SUBSCRIBE = CONTROLLER + "?" + CommandParams.COMMAND + "="
            + CommandParams.SUBSCRIBE;
}
