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

    private UserImpl(long id, @NotNull String name, @NotNull String surname, @NotNull String login,
                     @NotNull String phone, @NotNull Role role) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.phone = phone;
        this.role = role;
    }

    public static User newInstance(long id, @NotNull String name, @NotNull String surname, @NotNull String login,
                                   @NotNull String phone, @NotNull Role role) {
        return new UserImpl(id, name, surname, login, phone, role);
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
    public String getName() {
        return name;
    }

    @Override
    public String getSurname() {
        return surname;
    }

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public Role getRole() {
        return role;
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
