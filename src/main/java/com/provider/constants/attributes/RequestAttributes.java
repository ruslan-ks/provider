package com.provider.constants.attributes;

/**
 * Constant class containing request attribute names
 */
public class RequestAttributes {
    private RequestAttributes() {}

    public static final String PAGE_COUNT = "pageCount";

    public static final String USER_ACCOUNTS = "userAccounts";

    /**
     * Attribute of type {@code List<User>}
     */
    public static final String USERS = "users";

    /**
     * Attribute of type {@code List<Service>}
     */
    public static final String SERVICES = "services";

    /**
     * Attribute of type {@code List<TariffDto>}
     */
    public static final String TARIFFS = "tariffs";

    /**
     * Attribute of type {@code Map<String, String>}, where<br>
     * key: order defining enum value string representation obtained via .name(),
     * value: title that should be shown to user
     */
    public static final String TARIFF_ORDER_BY_FIELDS = "tariffOrderByFields";

    /**
     * Attribute of type {@code Map<Service, Integer>}, where<br>
     * key: service,<br>
     * value: count of tariffs that include this service
     */
    public static final String SERVICE_COUNT_MAP = "serviceCountMap";

    /**
     * Attribute of type {@code Iterable<Integer>}
     */
    public static final String USER_SUBSCRIBED_TARIFF_IDS = "userSubscribedTariffIds";

    /**
     * Attribute of type Iterable<{@link com.provider.entity.dto.SubscriptionTariffDto}>
     */
    public static final String USER_ACTIVE_SUBSCRIPTION_DTOS = "userSubscriptionDtos";

    /**
     * Attribute of type {@link com.provider.entity.dto.TariffDto}
     */
    public static final String TARIFF_DTO = "tariffDto";
}
