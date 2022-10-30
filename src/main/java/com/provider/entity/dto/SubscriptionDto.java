package com.provider.entity.dto;

import com.provider.entity.Entity;
import com.provider.entity.product.Tariff;
import com.provider.entity.product.TariffDuration;
import com.provider.entity.product.Subscription;
import com.provider.entity.user.UserAccount;
import org.jetbrains.annotations.NotNull;

public interface SubscriptionDto extends Entity {
    @NotNull Tariff getTariff();

    @NotNull TariffDuration getTariffDuration();

    @NotNull Subscription getSubscription();

    @NotNull UserAccount getUserAccount();
}
