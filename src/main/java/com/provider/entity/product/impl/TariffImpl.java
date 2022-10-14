package com.provider.entity.product.impl;

import com.provider.entity.product.Tariff;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Objects;

public class TariffImpl implements Tariff {
    private int id;
    private final String title;
    private final String description;
    private Status status;
    private final BigDecimal usdPrice;

    protected TariffImpl(int id, @NotNull String title, @NotNull String description, @NotNull Status status,
                         @NotNull BigDecimal usdPrice) {
        if (usdPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("usdPrice(" + usdPrice + ") < 0");
        }
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.usdPrice = usdPrice;
    }

    public static TariffImpl of(int id, @NotNull String title, @NotNull String description, @NotNull Status status,
                                @NotNull BigDecimal usdPrice) {
        return new TariffImpl(id, title, description, status, usdPrice);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException();
        }
        this.id = id;
    }

    @Override
    public @NotNull String getTitle() {
        return title;
    }

    @Override
    public @NotNull String getDescription() {
        return description;
    }

    @Override
    public @NotNull Status getStatus() {
        return status;
    }

    @Override
    public void setStatus(@NotNull Status newStatus) {
        if (!Status.FLOW.contains(Pair.of(status, newStatus))) {
            throw new IllegalStateException();
        }
        this.status = newStatus;
    }

    @Override
    public @NotNull BigDecimal getUsdPrice() {
        return usdPrice;
    }

    @Override
    public String toString() {
        return "TariffImpl{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", usdPrice=" + usdPrice +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TariffImpl tariff = (TariffImpl) o;
        return id == tariff.id
                && Objects.equals(title, tariff.title)
                && status == tariff.status
                && Objects.equals(usdPrice, tariff.usdPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, status, usdPrice);
    }
}
