package com.provider.entity.dto;

import com.provider.entity.product.Subscription;
import com.provider.entity.product.Tariff;
import com.provider.entity.product.TariffDuration;
import com.provider.entity.user.UserAccount;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SimpleSubscriptionDto implements SubscriptionDto {
    private final Tariff tariff;
    private final TariffDuration tariffDuration;
    private final Subscription subscription;
    private final UserAccount userAccount;

    SimpleSubscriptionDto(@NotNull Tariff tariff, @NotNull TariffDuration tariffDuration,
                          @NotNull Subscription subscription, @NotNull UserAccount userAccount) {
        this.tariff = tariff;
        this.tariffDuration = tariffDuration;
        this.subscription = subscription;
        this.userAccount = userAccount;
    }

    public static SimpleSubscriptionDto of(@NotNull Tariff tariff, @NotNull TariffDuration tariffDuration,
                                           @NotNull Subscription subscription, @NotNull UserAccount userAccount) {
        return new SimpleSubscriptionDto(tariff, tariffDuration, subscription, userAccount);
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
    public @NotNull UserAccount getUserAccount() {
        return userAccount;
    }

    @Override
    public String toString() {
        return "SimpleSubscriptionDto{" +
                "tariff=" + tariff +
                ", tariffDuration=" + tariffDuration +
                ", subscription=" + subscription +
                ", userAccount=" + userAccount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleSubscriptionDto that = (SimpleSubscriptionDto) o;
        return Objects.equals(tariff, that.tariff) && Objects.equals(tariffDuration, that.tariffDuration) && Objects.equals(subscription, that.subscription) && Objects.equals(userAccount, that.userAccount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tariff, tariffDuration, subscription, userAccount);
    }
}
