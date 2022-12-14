package com.provider.constants;

/**
 * Application(servlet context) init parameter names
 */
public class AppInitParams {
    private AppInitParams() {}

    /**
     * Default application locale: content of this locale is saved without locale info and is called 'default version'
     */
    public static final String DEFAULT_LOCALE = "defaultLocale";

    /**
     * Default user timezone - used when showing date and time
     */
    public static final String DEFAULT_USER_TIMEZONE = "defaultUserTimezone";

    /**
     * File upload directory path parameter.
     */
    public static final String FILE_UPLOAD_DIR = "fileUploadDir";
}
