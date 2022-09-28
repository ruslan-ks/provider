package com.provider.entity.user;

import com.provider.entity.Entity;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Set;

/**
 * User status interface
 */
public interface UserStatus extends Entity {
    /**
     * Possible user statuses
     */
    enum Status {
        ACTIVE,
        SUSPENDED
    }

    /**
     * Returns user id
     * @return user id - non-negative value
     */
    long getUserId();

    /**
     * Returns user status
     * @return usr status
     */
    @NotNull Status getStatus();

    /**
     * Sets status, checks if status change doesn't violate status flow defined by FLOW
     * @param status status to be set
     * @throws IllegalStateException if status change violates status flow defined by FLOW
     */
    void setStatus(Status status);

    /**
     * Returns comment specified when this status was set
     * @return status comment
     */
    @NotNull String getComment();

    /**
     * Returns status setting time
     * @return status setting time
     */
    @NotNull Instant getSetTime();

    /**
     * User status may change from first to second:
     * active -> suspended
     * suspended -> active
     */
    Set<Pair<Status, Status>> FLOW = Set.of(
            new ImmutablePair<>(Status.ACTIVE, Status.SUSPENDED),
            new ImmutablePair<>(Status.SUSPENDED, Status.ACTIVE)
    );

    /**
     * Returns true if status may be changed from fromStatus to toStatus
     * @param fromStatus initial status
     * @param toStatus new status
     * @return true if status may be changed from fromStatus to toStatus
     */
    static boolean isPossibleStatusChange(Status fromStatus, Status toStatus) {
        final Pair<Status, Status> statusChange = new ImmutablePair<>(fromStatus, toStatus);
        return FLOW.stream().anyMatch(statusChange::equals);
    }
}
