package com.provider.entity.product.impl;

import com.provider.entity.product.Tariff;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Objects;

public class TariffImpl implements Tariff {
    private int id;
    private String title;
    private String description;
    private Status status;
    private final BigDecimal usdPrice;
    private String imageFileName;

    protected TariffImpl(int id, @NotNull String title, @NotNull String description, @NotNull Status status,
                         @NotNull BigDecimal usdPrice, @NotNull String imageFileName) {
        if (usdPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("usdPrice(" + usdPrice + ") < 0");
        }
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.usdPrice = usdPrice;
        this.imageFileName = imageFileName;
    }

    public static TariffImpl of(int id, @NotNull String title, @NotNull String description, @NotNull Status status,
                                @NotNull BigDecimal usdPrice, @NotNull String imageFileName) {
        return new TariffImpl(id, title, description, status, usdPrice, imageFileName);
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
    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    @Override
    public @NotNull String getDescription() {
        return description;
    }

    @Override
    public void setDescription(@NotNull String description) {
        this.description = description;
    }

    @Override
    public @NotNull Status getStatus() {
        return status;
    }

    @Override
    public void setStatus(@NotNull Status newStatus) {
        if (newStatus == status) {
            return;
        }
        if (!Status.FLOW.contains(Pair.of(status, newStatus))) {
            throw new IllegalStateException();
        }
        status = newStatus;
    }

    @Override
    public @NotNull BigDecimal getUsdPrice() {
        return usdPrice;
    }

    @Override
    public @NotNull String getImageFileName() {
        return imageFileName;
    }

    @Override
    public void setImageFileName(@NotNull String imageFileName) {
        this.imageFileName = imageFileName;
    }

    @Override
    public String toString() {
        return "TariffImpl{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", usdPrice=" + usdPrice +
                ", imageFileName='" + imageFileName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TariffImpl tariff = (TariffImpl) o;
        return id == tariff.id && Objects.equals(title, tariff.title) && Objects.equals(description, tariff.description) && status == tariff.status && Objects.equals(usdPrice, tariff.usdPrice) && Objects.equals(imageFileName, tariff.imageFileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, status, usdPrice, imageFileName);
    }
}
