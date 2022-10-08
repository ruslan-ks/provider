package com.provider.entity.product.impl;

import com.provider.entity.product.TariffDuration;

import java.util.Objects;

public class TariffDurationImpl implements TariffDuration {
    private final int tariffId;
    private final int months;
    private final long minutes;

    protected TariffDurationImpl(int tariffId, int months, long minutes) {
        this.tariffId = tariffId;
        this.months = months;
        this.minutes = minutes;
    }

    public static TariffDurationImpl of(int tariffId, int months, long minutes) {
        return new TariffDurationImpl(tariffId, months, minutes);
    }

    @Override
    public int getTariffId() {
        return tariffId;
    }

    @Override
    public int getMonths() {
        return months;
    }

    @Override
    public long getMinutes() {
        return minutes;
    }

    @Override
    public String toString() {
        return "TariffDurationImpl{" +
                "tariffId=" + tariffId +
                ", months=" + months +
                ", minutes=" + minutes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TariffDurationImpl that = (TariffDurationImpl) o;
        return tariffId == that.tariffId
                && months == that.months
                && minutes == that.minutes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tariffId, months, minutes);
    }
}
