package com.provider.entity.dto;

import com.provider.entity.product.Tariff;
import com.provider.entity.product.TariffDuration;
import com.provider.entity.product.Subscription;
import org.jetbrains.annotations.NotNull;

public interface SubscriptionDto {
    @NotNull Tariff getTariff();

    @NotNull TariffDuration getTariffDuration();

    @NotNull Subscription getSubscription();
}
