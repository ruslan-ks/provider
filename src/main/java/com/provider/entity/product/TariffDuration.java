package com.provider.entity.product;

import com.provider.entity.Entity;

/**
 * Tariff duration entity
 */
public interface TariffDuration extends Entity {
    int getTariffId();

    /**
     * Sets tariff id
     * @param tariffId tariff id to be set
     * @throws IllegalArgumentException if {@code tariffId <= 0}
     */
    void setTariffId(int tariffId);

    int getMonths();

    long getMinutes();
}
