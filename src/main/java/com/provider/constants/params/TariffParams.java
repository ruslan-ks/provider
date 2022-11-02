package com.provider.constants.params;

public class TariffParams {
    private TariffParams() {}

    public static final String ID = "tariffId";
    public static final String TITLE = "tariffTitle";
    public static final String DESCRIPTION = "tariffDescription";
    public static final String USD_PRICE = "tariffUsdPrice";
    public static final String DURATION_MONTHS = "tariffDurationMonths";
    public static final String DURATION_MINUTES = "tariffDurationMinutes";

    /**
     * Parameter value is one of {@link com.provider.entity.product.Tariff.Status}.values()
     */
    public static final String STATUS = "tariffStatus";

    /**
     * Tariff services ids(array) parameter name
     */
    public static final String SERVICE_IDS = "tariffServiceIds";

    /**
     * Image multipart data parameter name
     */
    public static final String IMAGE = "tariffImage";

    /**
     * Tariff image file name(used to store the image somewhere) parameter name
     */
    public static final String IMAGE_FILE_NAME = "tariffImageFileName";
}
