package com.provider.constants.attributes;

/**
 * Constant class containing application scope(servlet context) attribute names.
 */
public class AppAttributes {
    private AppAttributes() {}

    /**
     * Path to the directory where tariff images are uploaded
     * Parameter type: {@code java.util.Map<String, String>} where key - locale, value - lang
     */
    public static final String LOCALE_LANG_MAP = "localeLangMap";

    /**
     * <strong>Real</strong> path to the directory where tariff images are uploaded
     * Parameter type: {@code java.lang.String}
     */
    public static final String TARIFF_IMAGE_UPLOAD_PATH = "tariffImageUploadPath";

    /**
     * Directory where tariff images are stored; relative to the app context path
     * Parameter type: {@code java.lang.String}
     */
    public static final String TARIFF_IMAGE_DIR = "tariffImageDir";
}
