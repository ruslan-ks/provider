package com.provider.constants.params;

/**
 * Replenish parameter name
 */
public class ReplenishParams {
    private ReplenishParams() {}

    /**
     * Money amount. Floating(2 signs after comma)
     */
    public static final String AMOUNT = "amount";

    /**
     * Value obtained from {@link com.provider.entity.Currency#name()} call<br>
     * So Currency object may be obtained through {@code Currency.valueOf()} call
     */
    public static final String CURRENCY = "currency";
}
