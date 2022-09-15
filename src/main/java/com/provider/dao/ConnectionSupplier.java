package com.provider.dao;

import com.provider.dao.exception.DBException;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

public interface ConnectionSupplier {
    @NotNull Connection get() throws DBException;
}
