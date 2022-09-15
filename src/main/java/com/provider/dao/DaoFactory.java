package com.provider.dao;

import org.jetbrains.annotations.NotNull;

public interface DaoFactory {
    @NotNull UserDao newUserDao();
    @NotNull UserPasswordDao newUserPasswordDao();
}
