package com.provider.entity.product;

import com.provider.entity.Entity;

/**
 * Tariff duration entity
 */
public interface TariffDuration extends Entity {
    int getTariffId();

    int getMonths();

    long getMinutes();
}
