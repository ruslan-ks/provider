package com.provider.entity.user.impl;

import com.provider.entity.user.UserStatus;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Objects;

public class UserStatusImpl implements UserStatus {
    private final long userId;
    private Status status;
    private final String comment;
    private final Instant setTime;

    private UserStatusImpl(long userId, @NotNull Status status, @NotNull String comment,
                           @NotNull Instant setTime) {
        if (userId < 0) {
            throw new IllegalArgumentException("User id(" + userId + ") < 0");
        }
        this.userId = userId;
        this.status = status;
        this.comment = comment;
        this.setTime = setTime;
    }

    public static @NotNull UserStatusImpl newInstance(long userId, @NotNull Status status, @NotNull String comment,
                                                      @NotNull Instant setTime) {
        return new UserStatusImpl(userId, status, comment, setTime);
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public @NotNull Status getStatus() {
        return status;
    }

    @Override
    public void setStatus(Status newStatus) {
        if (UserStatus.isPossibleStatusChange(this.status, newStatus)) {
            this.status = newStatus;
        }
        throw new IllegalStateException();
    }

    @Override
    public @NotNull String getComment() {
        return comment;
    }

    @Override
    public @NotNull Instant getSetTime() {
        return setTime;
    }

    @Override
    public String toString() {
        return "UserStatusImpl{" +
                "userId=" + userId +
                ", status=" + status +
                ", comment='" + comment + '\'' +
                ", setTime=" + setTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserStatusImpl that = (UserStatusImpl) o;
        return userId == that.userId
                && status == that.status
                && Objects.equals(comment, that.comment)
                && Objects.equals(setTime, that.setTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, status, comment, setTime);
    }
}
