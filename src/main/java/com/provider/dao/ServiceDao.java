package com.provider.dao;

import com.provider.dao.exception.DBException;
import com.provider.entity.product.Service;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class ServiceDao extends EntityDao<Integer, Service> {
    public abstract @NotNull List<Service> findAll() throws DBException;
}
