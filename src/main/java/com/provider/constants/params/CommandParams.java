package com.provider.constants.params;

/**
 * Command parameter names
 */
public class CommandParams {
    private CommandParams() {}

    /**
     * Command parameter name
     */
    public static final String COMMAND = "command";

    /**
     * Sign in command
     */
    public static final String SIGN_IN = "signIn";

    /**
     * Sign out command
     */
    public static final String SIGN_OUT = "signOut";

    /**
     * Get user panel page command
     */
    public static final String USER_PANEL = "userPanel";

    /**
     * Get replenish page command
     */
    public static final String REPLENISH_PAGE = "replenishPage";

    /**
     * Replenish account command
     */
    public static final String REPLENISH = "replenish";

    /**
     * Users management page access command
     */
    public static final String USERS_MANAGEMENT_PAGE = "usersManagementPage";

    /**
     * Add new user command
     */
    public static final String ADD_USER = "addUser";

    /**
     * Switch user status(active/suspended) command
     */
    public static final String UPDATE_USER_STATUS = "updateUserStatus";

    /**
     * Tariffs management page access command
     */
    public static final String TARIFFS_MANAGEMENT_PAGE = "tariffsManagementPage";

    /**
     * Add service action command
     */
    public static final String ADD_SERVICE = "addService";

    /**
     * Add tariff action command
     */
    public static final String ADD_TARIFF = "addTariff";

    /**
     * Catalog page access command
     */
    public static final String CATALOG_PAGE = "catalogPage";

    /**
     * Buy tariff action command
     */
    public static final String SUBSCRIBE = "subscribe";

    /**
     * Unsubscribe action command
     */
    public static final String UNSUBSCRIBE = "unsubscribe";

    /**
     * Download pdf command
     */
    public static final String DOWNLOAD_TARIFF_PDF = "downloadTariffPdf";

    /**
     * Edit tariff page command
     */
    public static final String EDIT_TARIFF_PAGE = "editTariffPage";

    /**
     * Edit tariff command
     */
    public static final String EDIT_TARIFF = "editTariff";

    /**
     * Change tariff image command
     */
    public static final String CHANGE_TARIFF_IMAGE = "changeTariffImage";

    /**
     * Tariff image command - writes image data to the response body
     */
    public static final String TARIFF_IMAGE = "tariffImage";
}
