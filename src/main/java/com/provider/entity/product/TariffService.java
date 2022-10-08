package com.provider.entity.product;

import com.provider.entity.Entity;

/**
 * Many-to-many relation
 */
public interface TariffService extends Entity {
    int getTariffId();
    int getServiceId();
}
