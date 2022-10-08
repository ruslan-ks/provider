package com.provider.entity.product.impl;

import com.provider.entity.product.Service;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ServiceImpl implements Service {
    private int id;
    private final String name;

    protected ServiceImpl(int id, @NotNull String name) {
        this.id = id;
        this.name = name;
    }

    public static ServiceImpl of(int id, @NotNull String name) {
        return new ServiceImpl(id, name);
    }

    @Override
    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException();
        }
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ServiceImpl{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceImpl service = (ServiceImpl) o;
        return id == service.id && Objects.equals(name, service.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
