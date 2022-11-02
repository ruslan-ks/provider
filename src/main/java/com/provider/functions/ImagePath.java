package com.provider.functions;

import com.provider.constants.Paths;
import com.provider.constants.params.CommandParams;
import com.provider.constants.params.TariffParams;
import com.provider.util.ParameterizedUrl;

public class ImagePath {
    private ImagePath() {}

    public static String tariffImageUrl(String imageName) {
        final ParameterizedUrl url = ParameterizedUrl.of(Paths.CONTROLLER);
        url.addParam(CommandParams.COMMAND, CommandParams.TARIFF_IMAGE);
        url.addParam(TariffParams.IMAGE_FILE_NAME, imageName);
        return url.getString();
    }
}
