package com.provider.entity.dto;

import com.provider.entity.Entity;
import com.provider.entity.product.Service;
import com.provider.entity.product.Tariff;
import com.provider.entity.product.TariffDuration;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Tariff data transfer object
 */
public interface TariffDto extends Entity {
    @NotNull Tariff getTariff();

    @NotNull TariffDuration getTariffDuration();

    @NotNull List<Service> getTariffServices();
}
