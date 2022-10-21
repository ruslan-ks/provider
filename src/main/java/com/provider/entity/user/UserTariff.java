package com.provider.entity.user;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public interface UserTariff {
    long getUserAccountId();
    int getTariffId();

    /**
     * Returns time of tariff subscription
     * @return time of tariff subscription
     */
    @NotNull Instant subscriptionTime();
}
