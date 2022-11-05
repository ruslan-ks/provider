package com.provider.dao.postgres;

import com.provider.dao.ConnectionSupplier;
import org.jetbrains.annotations.NotNull;

public class PostgresTestDaoFactory extends PostgresDaoFactory {
    private final ConnectionSupplier connectionSupplier = new PostgresTestConnectionSupplier();

    @Override
    public @NotNull ConnectionSupplier newConnectionSupplier() {
        return connectionSupplier;
    }
}
