package com.provider.entity.product;

import com.provider.entity.Entity;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Tariff(actually tariff plan) entity
 */
public interface Tariff extends Entity {
    /**
     * Tariff statuses
     */
    enum Status {
        ACTIVE,
        HIDDEN;

        /**
         * From-to pairs describing possible status changes
         * Status may be changed only according to the FLOW.
         */
        public static final Set<Pair<Status, Status>> FLOW = Set.of(Pair.of(ACTIVE, HIDDEN), Pair.of(HIDDEN, ACTIVE));
    }

    int getId();

    /**
     * Sets new id
     * @param id new id
     * @throws IllegalArgumentException if id <= 0
     */
    void setId(int id);

    @NotNull String getTitle();

    void setTitle(@NotNull String title);

    @NotNull String getDescription();

    void setDescription(@NotNull String description);

    @NotNull Status getStatus();

    /**
     * Sets new status
     * @param status new status to be set
     * @throws IllegalStateException if status change is not allowed
     * (if Status.FLOW does not contain pair(current, new))
     */
    void setStatus(@NotNull Status status);

    @NotNull BigDecimal getUsdPrice();

    @NotNull String getImageFileName();

    void setImageFileName(@NotNull String imageFileName);
}
