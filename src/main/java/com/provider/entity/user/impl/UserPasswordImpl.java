package com.provider.entity.user.impl;

import com.provider.entity.user.UserPassword;
import com.provider.entity.user.hashing.PasswordHashing;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class UserPasswordImpl implements UserPassword {
    private long userId;
    private final String data;
    private final String salt;
    private final PasswordHashing.HashMethod hashMethod;

    private UserPasswordImpl(long userId, @NotNull String data, @NotNull String salt,
                             @NotNull PasswordHashing.HashMethod hashMethod) {
        this.userId = userId;
        this.data = data;
        this.salt = salt;
        this.hashMethod = hashMethod;
    }

    /**
     * Creates new UserPasswordImpl instance
     * @param userId user id
     * @param hash hashed password
     * @param salt salt used when hashing the password
     * @param hashMethod method used to generate the hash
     * @return new UserPasswordImpl instance
     */
    public static UserPasswordImpl of(long userId, @NotNull String hash, @NotNull String salt,
                                      @NotNull PasswordHashing.HashMethod hashMethod) {
        return new UserPasswordImpl(userId, hash, salt, hashMethod);
    }

    /**
     * Creates new UserPasswordImpl instance with id == 0
     * @param hash hashed password
     * @param salt salt used when hashing the password
     * @param hashMethod method used to generate the hash
     * @return new UserPasswordImpl instance
     */
    public static UserPasswordImpl of(@NotNull String hash, @NotNull String salt,
                                      @NotNull PasswordHashing.HashMethod hashMethod) {
        return new UserPasswordImpl(0, hash, salt, hashMethod);
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(long userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException();
        }
        this.userId = userId;
    }

    @Override
    public @NotNull String getHash() {
        return data;
    }

    @Override
    public @NotNull String getSalt() {
        return salt;
    }

    @Override
    public @NotNull PasswordHashing.HashMethod getHashMethod() {
        return hashMethod;
    }

    @Override
    public String toString() {
        return "UserPasswordImpl{" +
                "userId=" + userId +
                ", data='" + data + '\'' +
                ", salt='" + salt + '\'' +
                ", hashMethod=" + hashMethod +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPasswordImpl that = (UserPasswordImpl) o;
        return userId == that.userId
                && Objects.equals(data, that.data)
                && Objects.equals(salt, that.salt)
                && hashMethod == that.hashMethod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, data, salt, hashMethod);
    }
}
