package com.provider.entity.product.impl;

import com.provider.entity.product.TariffDuration;

import java.util.Objects;

public class TariffDurationImpl implements TariffDuration {
    private int tariffId;
    private final int months;
    private final int minutes;

    protected TariffDurationImpl(int tariffId, int months, int minutes) {
        this.tariffId = tariffId;
        this.months = months;
        this.minutes = minutes;
    }

    public static TariffDurationImpl of(int tariffId, int months, int minutes) {
        return new TariffDurationImpl(tariffId, months, minutes);
    }

    @Override
    public int getTariffId() {
        return tariffId;
    }

    @Override
    public void setTariffId(int tariffId) {
        if (tariffId <= 0) {
            throw new IllegalArgumentException("tariffId <= 0: tariffId = " + tariffId);
        }
        this.tariffId = tariffId;
    }

    @Override
    public int getMonths() {
        return months;
    }

    @Override
    public int getMinutes() {
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
