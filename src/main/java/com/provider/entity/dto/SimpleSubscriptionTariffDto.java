package com.provider.entity.dto;

import com.provider.entity.product.Subscription;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SimpleSubscriptionTariffDto implements SubscriptionTariffDto {
    private final Subscription subscription;
    private final TariffDto tariffDto;

    SimpleSubscriptionTariffDto(@NotNull Subscription subscription, @NotNull TariffDto tariffDto) {
        this.subscription = subscription;
        this.tariffDto = tariffDto;
    }

    public static @NotNull SimpleSubscriptionTariffDto of(@NotNull Subscription subscription,
                                                          @NotNull TariffDto tariffDto) {
        return new SimpleSubscriptionTariffDto(subscription, tariffDto);
    }

    @Override
    public @NotNull Subscription getSubscription() {
        return subscription;
    }

    @Override
    public @NotNull TariffDto getTariffDto() {
        return tariffDto;
    }

    @Override
    public String toString() {
        return "SimpleSubscriptionTariffDto{" +
                "subscription=" + subscription +
                ", tariffDto=" + tariffDto +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleSubscriptionTariffDto that = (SimpleSubscriptionTariffDto) o;
        return Objects.equals(subscription, that.subscription) && Objects.equals(tariffDto, that.tariffDto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subscription, tariffDto);
    }
}
