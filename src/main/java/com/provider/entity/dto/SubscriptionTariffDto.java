package com.provider.entity.dto;

import com.provider.entity.product.Subscription;
import org.jetbrains.annotations.NotNull;

public interface SubscriptionTariffDto {
    @NotNull Subscription getSubscription();

    @NotNull TariffDto getTariffDto();
}
