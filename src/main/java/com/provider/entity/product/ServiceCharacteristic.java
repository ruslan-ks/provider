package com.provider.entity.product;

import com.provider.entity.Entity;
import org.jetbrains.annotations.NotNull;

/**
 * One service has many characteristics(name-value pairs)
 */
public interface ServiceCharacteristic extends Entity {
    int getServiceId();
    @NotNull String getName();
    @NotNull String getValue();
}
