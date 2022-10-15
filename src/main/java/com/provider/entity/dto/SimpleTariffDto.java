package com.provider.entity.dto;

import com.provider.entity.product.Service;
import com.provider.entity.product.Tariff;
import com.provider.entity.product.TariffDuration;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class SimpleTariffDto implements TariffDto {
    private final Tariff tariff;
    private final TariffDuration tariffDuration;
    private final List<Service> serviceList;

    SimpleTariffDto(@NotNull Tariff tariff, @NotNull TariffDuration tariffDuration,
                    @NotNull List<Service> serviceList) {
        this.tariff = tariff;
        this.tariffDuration = tariffDuration;
        this.serviceList = List.copyOf(serviceList);
    }

    public static SimpleTariffDto of(@NotNull Tariff tariff, @NotNull TariffDuration tariffDuration,
                                     @NotNull List<Service> serviceList) {
        return new SimpleTariffDto(tariff, tariffDuration, serviceList);
    }

    @Override
    public @NotNull Tariff getTariff() {
        return tariff;
    }

    @Override
    public @NotNull TariffDuration getDuration() {
        return tariffDuration;
    }

    @Override
    public @NotNull List<Service> getServices() {
        return serviceList;
    }

    @Override
    public String toString() {
        return "SimpleTariffDto{" +
                "tariff=" + tariff +
                ", tariffDuration=" + tariffDuration +
                ", serviceList=" + serviceList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleTariffDto that = (SimpleTariffDto) o;
        return Objects.equals(tariff, that.tariff)
                && Objects.equals(tariffDuration, that.tariffDuration)
                && Objects.equals(serviceList, that.serviceList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tariff, tariffDuration, serviceList);
    }
}
