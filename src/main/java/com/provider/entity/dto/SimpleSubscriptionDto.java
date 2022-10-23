package com.provider.entity.dto;

import com.provider.entity.product.Subscription;
import com.provider.entity.product.Tariff;
import com.provider.entity.product.TariffDuration;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SimpleSubscriptionDto implements SubscriptionDto {
    private final Tariff tariff;
    private final TariffDuration tariffDuration;
    private final Subscription subscription;

    SimpleSubscriptionDto(@NotNull Tariff tariff, TariffDuration tariffDuration, Subscription subscription) {
        this.tariff = tariff;
        this.tariffDuration = tariffDuration;
        this.subscription = subscription;
    }

    public static SimpleSubscriptionDto of(@NotNull Tariff tariff, TariffDuration tariffDuration,
                                           Subscription subscription) {
        return new SimpleSubscriptionDto(tariff, tariffDuration, subscription);
    }

    @Override
    public @NotNull Tariff getTariff() {
        return tariff;
    }

    @Override
    public @NotNull TariffDuration getTariffDuration() {
        return tariffDuration;
    }

    @Override
    public @NotNull Subscription getSubscription() {
        return subscription;
    }

    @Override
    public String toString() {
        return "SimpleSubscriptionDto{" +
                "tariff=" + tariff +
                ", tariffDuration=" + tariffDuration +
                ", subscription=" + subscription +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleSubscriptionDto that = (SimpleSubscriptionDto) o;
        return Objects.equals(tariff, that.tariff)
                && Objects.equals(tariffDuration, that.tariffDuration)
                && Objects.equals(subscription, that.subscription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tariff, tariffDuration, subscription);
    }
}
