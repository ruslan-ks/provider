package com.provider.functions;

import com.provider.constants.attributes.AppAttributes;
import jakarta.servlet.ServletContext;

public class ImagePath {
    private ImagePath() {}

    public static String tariffImagePath(String imageName, ServletContext appContext) {
        return appContext.getAttribute(AppAttributes.TARIFF_IMAGE_DIR) + "/" + imageName;
    }
}
