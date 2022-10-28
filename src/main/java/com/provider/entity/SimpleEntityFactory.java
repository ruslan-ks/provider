package com.provider.entity;

import com.provider.entity.product.*;
import com.provider.entity.product.impl.*;
import com.provider.entity.user.User;
import com.provider.entity.user.UserAccount;
import com.provider.entity.user.UserPassword;
import com.provider.entity.user.hashing.PasswordHashing;
import com.provider.entity.user.impl.UserAccountImpl;
import com.provider.entity.user.impl.UserImpl;
import com.provider.entity.user.impl.UserPasswordImpl;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public class SimpleEntityFactory implements EntityFactory {
    protected SimpleEntityFactory() {}

    public static SimpleEntityFactory newInstance() {
        return new SimpleEntityFactory();
    }

    @Override
    public @NotNull User newUser(long id, @NotNull String name, @NotNull String surname, @NotNull String login,
                                 @NotNull String phone, User.@NotNull Role role, User.@NotNull Status status) {
        return UserImpl.of(id, name, surname, login, phone, role, status);
    }

    @Override
    public @NotNull UserAccount newUserAccount(long id, long userId, @NotNull Currency currency) {
        return UserAccountImpl.of(id, userId, currency);
    }

    @Override
    public @NotNull UserAccount newUserAccount(long id, long userId, @NotNull Currency currency,
                                               @NotNull BigDecimal amount) {
        return UserAccountImpl.of(id, userId, currency, amount);
    }

    @Override
    public @NotNull UserPassword newUserPassword(long userId, @NotNull String hash, @NotNull String salt,
                                                 @NotNull PasswordHashing.HashMethod hashMethod) {
        return UserPasswordImpl.of(userId, hash, salt, hashMethod);
    }

    @Override
    public @NotNull UserPassword newUserPassword(@NotNull String hash, @NotNull String salt,
                                                 @NotNull PasswordHashing.HashMethod hashMethod) {
        return UserPasswordImpl.of(hash, salt, hashMethod);
    }

    @Override
    public @NotNull Service newService(int id, @NotNull String name, @NotNull String description) {
        return ServiceImpl.of(id, name, description);
    }

    @Override
    public @NotNull TariffDuration newTariffDuration(int tariffId, int months, int minutes) {
        return TariffDurationImpl.of(tariffId, months, minutes);
    }

    @Override
    public @NotNull Tariff newTariff(int id, @NotNull String title,
                                     @NotNull String description,
                                     @NotNull Tariff.Status status,
                                     @NotNull BigDecimal usdPrice,
                                     @NotNull String imageFileName) {
        return TariffImpl.of(id, title, description, status, usdPrice, imageFileName);
    }

    @Override
    public @NotNull Subscription newSubscription(long id, long userAccountId, int tariffId, @NotNull Instant startTime,
                                                 @NotNull Instant lastPaymentTime, Subscription.@NotNull Status status) {
        return SubscriptionImpl.of(id, userAccountId, tariffId, startTime, lastPaymentTime, status);
    }
}
