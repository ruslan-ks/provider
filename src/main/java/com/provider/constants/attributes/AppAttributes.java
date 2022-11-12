package com.provider.constants.attributes;

/**
 * Constant class containing application scope(servlet context) attribute names.
 */
public class AppAttributes {
    private AppAttributes() {}

    /**
     * Parameter type: {@code java.util.Map<String, String>} where key - locale, value - user friendly language name
     */
    public static final String LOCALE_LANG_MAP = "localeLangMap";

    /**
     * <strong>Real</strong> path to the directory where tariff images are uploaded
     * Parameter type: {@code java.lang.String}
     */
    public static final String TARIFF_IMAGE_UPLOAD_DIR_PATH = "tariffImageUploadPath";
}
