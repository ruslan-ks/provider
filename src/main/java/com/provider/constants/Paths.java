package com.provider.constants;

import com.provider.controller.command.FrontCommand;

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
     * Front controller servlet
     */
    private static final String CONTROLLER = "controller";

    /**
     * Command parameter name
     */
    public static final String COMMAND = "command";

    /**
     * Sign in controller action
     */
    public static final String SIGN_IN = CONTROLLER + "?" + COMMAND + "=" + FrontCommand.SIGN_IN;

    /**
     * Sign out controller action
     */
    public static final String SIGN_OUT = CONTROLLER + "?" + COMMAND + "=" + FrontCommand.SIGN_OUT;

    /**
     *
     */
    public static final String REPLENISH = CONTROLLER + "?" + COMMAND + "=" + FrontCommand.REPLENISH;

    /**
     * User panel controller action
     */
    public static final String USER_PANEL_PAGE = CONTROLLER + "?" + COMMAND + "=" + FrontCommand.USER_PANEL;

    /**
     * Replenish page - allows to replenish user account
     */
    public static final String REPLENISH_PAGE = CONTROLLER + "?" + COMMAND + "=" + FrontCommand.REPLENISH_PAGE;
}
