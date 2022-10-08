package com.provider.entity.product.impl;

import com.provider.entity.product.ServiceCharacteristic;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ServiceCharacteristicImpl implements ServiceCharacteristic {
    private final int serviceId;
    private final String name;
    private final String value;

    protected ServiceCharacteristicImpl(int serviceId, @NotNull String name, @NotNull String value) {
        this.serviceId = serviceId;
        this.name = name;
        this.value = value;
    }

    public static ServiceCharacteristicImpl of(int serviceId, @NotNull String name, @NotNull String value) {
        return new ServiceCharacteristicImpl(serviceId, name, value);
    }

    @Override
    public int getServiceId() {
        return serviceId;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ServiceCharacteristicImpl{" +
                "serviceId=" + serviceId +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceCharacteristicImpl that = (ServiceCharacteristicImpl) o;
        return serviceId == that.serviceId
                && Objects.equals(name, that.name)
                && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceId, name, value);
    }
}
