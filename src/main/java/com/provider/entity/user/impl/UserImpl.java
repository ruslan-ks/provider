package com.provider.entity.user.impl;

import com.provider.entity.user.User;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class UserImpl implements User {
    private long id;
    private final String name;
    private final String surname;
    private final String login;
    private final String phone;
    private final Role role;
    private final Status status;

    private UserImpl(long id, @NotNull String name, @NotNull String surname, @NotNull String login,
                     @NotNull String phone, @NotNull Role role, @NotNull Status status) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.phone = phone;
        this.role = role;
        this.status = status;
    }

    public static User of(long id, @NotNull String name, @NotNull String surname, @NotNull String login,
                          @NotNull String phone, @NotNull Role role, @NotNull Status status) {
        return new UserImpl(id, name, surname, login, phone, role, status);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException();
        }
        this.id = id;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull String getSurname() {
        return surname;
    }

    @Override
    public @NotNull String getLogin() {
        return login;
    }

    @Override
    public @NotNull String getPhone() {
        return phone;
    }

    @Override
    public @NotNull Role getRole() {
        return role;
    }

    @Override
    public @NotNull Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "UserImpl{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", login='" + login + '\'' +
                ", phone='" + phone + '\'' +
                ", role=" + role +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserImpl user = (UserImpl) o;
        return id == user.id
                && Objects.equals(name, user.name)
                && Objects.equals(surname, user.surname)
                && Objects.equals(login, user.login)
                && Objects.equals(phone, user.phone)
                && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, login, phone, role);
    }
}
