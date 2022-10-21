package com.provider.entity.user.impl;

import com.provider.entity.user.UserTariff;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public class UserTariffImpl implements UserTariff {
    private final long userAccountId;
    private final int tariffId;
    private final Instant subscriptionTime;

    UserTariffImpl(long userAccountId, int tariffId, @NotNull Instant subscriptionTime) {
        this.userAccountId = userAccountId;
        this.tariffId = tariffId;
        this.subscriptionTime = subscriptionTime;
    }

    public static UserTariffImpl of(long userAccountId, int tariffId, @NotNull Instant subscriptionTime) {
        return new UserTariffImpl(userAccountId, tariffId, subscriptionTime);
    }

    @Override
    public long getUserAccountId() {
        return userAccountId;
    }

    @Override
    public int getTariffId() {
        return tariffId;
    }

    @Override
    public @NotNull Instant subscriptionTime() {
        return subscriptionTime;
    }
}
