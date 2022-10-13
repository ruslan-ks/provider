package com.provider.entity.product;

import com.provider.entity.Entity;
import org.jetbrains.annotations.NotNull;

/**
 * Service offered by the Provider, for example: TV, Internet, Mobile network
 */
public interface Service extends Entity {
    /**
     * Sets service id
     * @param id new id
     * @throws IllegalArgumentException if id <= 0
     */
    void setId(int id);

    int getId();

    @NotNull String getName();

    @NotNull String getDescription();
}
