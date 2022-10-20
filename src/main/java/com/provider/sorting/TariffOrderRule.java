package com.provider.sorting;

import org.jetbrains.annotations.NotNull;

public class TariffOrderRule {
    private final TariffOrderByField orderByField;
    private final boolean desc;

    private TariffOrderRule(@NotNull TariffOrderByField orderByField, boolean desc) {
        this.orderByField = orderByField;
        this.desc = desc;
    }

    public static TariffOrderRule of(@NotNull TariffOrderByField orderByField, boolean desc) {
        return new TariffOrderRule(orderByField, desc);
    }

    public TariffOrderByField getOrderByField() {
        return orderByField;
    }

    public boolean isDesc() {
        return desc;
    }
}
