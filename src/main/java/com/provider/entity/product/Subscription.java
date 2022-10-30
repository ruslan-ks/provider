package com.provider.entity.product;

import com.provider.entity.Entity;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Set;

public interface Subscription extends Entity {
    /**
     * Subscription status: <br>
     * ACTIVE: is currently in use <br>
     * INACTIVE: isn't used anymore <br>
     */
    enum Status {
        ACTIVE,
        INACTIVE;

        public static final Set<Pair<Status, Status>> FLOW = Set.of(
            Pair.of(ACTIVE, INACTIVE)
        );
    }

    long getId();

    /**
     * Sets id
     * @param id new id
     * @throws IllegalArgumentException if {@code id <= 0}
     */
    void setId(long id);

    /**
     * Does exactly what you think
     * @param lastPaymentTime time to be set
     * @throws IllegalArgumentException if new lastPaymentTime is before current value
     */
    void setLastPaymentTime(@NotNull Instant lastPaymentTime);

    long getUserAccountId();

    int getTariffId();

    @NotNull Instant getStartTime();

    @NotNull Instant getLastPaymentTime();

    @NotNull Status getStatus();

    /**
     * Sets specified status
     * @param status status to be set
     * @throws IllegalArgumentException if Status.FLOW does not have Pair of current status and the new one.
     */
    void setStatus(@NotNull Status status);
}
