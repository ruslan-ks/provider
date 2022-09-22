package com.provider.constants;

import com.provider.constants.params.CommandParams;

/**
 * Resource paths relative to the project root.
 * Page paths are suffixed with _PAGE, controller action paths are not.
 */
public class Paths {
    private Paths() {}

    /**
     * Sign in JSP page
     */
    public static final String SIGN_IN_PAGE = "signIn";

    /**
     * User panel page - hidden under /WEB-INF/
     */
    public static final String USER_PANEL_PAGE = "/WEB-INF/pages/userPanel.jsp";

    /**
     * Front controller servlet
     */
    public static final String CONTROLLER = "controller";

    /**
     * Sign in controller action
     */
    public static final String SIGN_IN = CONTROLLER + "?" + CommandParams.COMMAND + "=" + CommandParams.SIGN_IN;

    /**
     * User panel controller action
     */
    public static final String USER_PANEL = CONTROLLER + "?" + CommandParams.COMMAND + "=" + CommandParams.USER_PANEL;

    /**
     * Sign out controller action
     */
    public static final String SIGN_OUT = CONTROLLER + "?" + CommandParams.COMMAND + "=" + CommandParams.SIGN_OUT;
}
