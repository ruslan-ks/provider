package com.provider.entity.product.impl;

import com.provider.entity.product.TariffService;

import java.util.Objects;

public class TariffServiceImpl implements TariffService {
    private final int tariffId;
    private final int serviceId;

    protected TariffServiceImpl(int tariffId, int serviceId) {
        this.tariffId = tariffId;
        this.serviceId = serviceId;
    }

    public static TariffServiceImpl of(int tariffId, int serviceId) {
        return new TariffServiceImpl(tariffId, serviceId);
    }

    @Override
    public int getTariffId() {
        return tariffId;
    }

    @Override
    public int getServiceId() {
        return serviceId;
    }

    @Override
    public String toString() {
        return "TariffServiceImpl{" +
                "tariffId=" + tariffId +
                ", serviceId=" + serviceId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TariffServiceImpl that = (TariffServiceImpl) o;
        return tariffId == that.tariffId && serviceId == that.serviceId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tariffId, serviceId);
    }
}
