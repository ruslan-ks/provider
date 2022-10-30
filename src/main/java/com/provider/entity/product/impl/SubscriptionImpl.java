package com.provider.entity.product.impl;

import com.provider.entity.product.Subscription;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Objects;

public class SubscriptionImpl implements Subscription {
    private long id;
    private final long userAccountId;
    private final int tariffId;
    private final Instant startTime;
    private Instant lastPaymentTime;
    private Status status;

    SubscriptionImpl(long id, long userAccountId, int tariffId, @NotNull Instant startTime,
                     @NotNull Instant lastPaymentTime, @NotNull Status status) {
        this.id = id;
        this.userAccountId = userAccountId;
        this.tariffId = tariffId;
        this.startTime = startTime;
        this.lastPaymentTime = lastPaymentTime;
        this.status = status;
    }

    public static SubscriptionImpl of(long id, long userAccountId, int tariffId, @NotNull Instant startTime,
                                      @NotNull Instant lastPaymentTime, @NotNull Status status) {
        return new SubscriptionImpl(id, userAccountId, tariffId, startTime, lastPaymentTime, status);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Failed to set id! Illegal new id! New id is <= 0: id = " + id);
        }
        this.id = id;
    }

    @Override
    public void setLastPaymentTime(@NotNull Instant lastPaymentTime) {
        if (lastPaymentTime.isBefore(this.lastPaymentTime)) {
            throw new IllegalArgumentException(lastPaymentTime + " is before current value(" + this.lastPaymentTime +
                    ")");
        }
        this.lastPaymentTime = lastPaymentTime;
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
    public @NotNull Instant getStartTime() {
        return startTime;
    }

    @Override
    public @NotNull Instant getLastPaymentTime() {
        return lastPaymentTime;
    }

    @Override
    public @NotNull Status getStatus() {
        return status;
    }

    @Override
    public void setStatus(@NotNull Status status) {
        if (!Status.FLOW.contains(Pair.of(this.status, status))) {
            throw new IllegalArgumentException("Illegal status change: Status change not allowed: " +
                    this.status + " -> " + status);
        }
        this.status = status;
    }

    @Override
    public String toString() {
        return "SubscriptionImpl{" +
                "id=" + id +
                ", userAccountId=" + userAccountId +
                ", tariffId=" + tariffId +
                ", startTime=" + startTime +
                ", lastPaymentTime=" + lastPaymentTime +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubscriptionImpl that = (SubscriptionImpl) o;
        return id == that.id
                && userAccountId == that.userAccountId
                && tariffId == that.tariffId
                && Objects.equals(startTime, that.startTime)
                && Objects.equals(lastPaymentTime, that.lastPaymentTime)
                && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userAccountId, tariffId, startTime, lastPaymentTime, status);
    }
}
