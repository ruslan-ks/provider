package com.provider.entity.user.impl;

import com.provider.entity.Currency;
import com.provider.entity.user.UserAccount;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Objects;

public class UserAccountImpl implements UserAccount {
    private long id;
    private final long userId;
    private final Currency currency;
    private final BigDecimal amount;

    UserAccountImpl(long id, long userId, @NotNull Currency currency) {
        this(id, userId, currency, BigDecimal.ZERO);
    }

    UserAccountImpl(long id, long userId, @NotNull Currency currency, @NotNull BigDecimal amount) {
        this.id = id;
        this.userId = userId;
        this.currency = currency;
        this.amount = amount;
    }

    public static @NotNull UserAccountImpl newInstance(long id, long userId, @NotNull Currency currency,
                                                       @NotNull BigDecimal amount) {
        return new UserAccountImpl(id, userId, currency, amount);
    }

    public static @NotNull UserAccountImpl newInstance(long id, long userId, @NotNull Currency currency) {
        return new UserAccountImpl(id, userId, currency);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public @NotNull Currency getCurrency() {
        return currency;
    }

    @Override
    public @NotNull BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "UserAccountImpl{" +
                "id=" + id +
                ", userId=" + userId +
                ", currency=" + currency +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccountImpl that = (UserAccountImpl) o;
        return id == that.id && userId == that.userId && currency == that.currency
                && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, currency, amount);
    }
}
