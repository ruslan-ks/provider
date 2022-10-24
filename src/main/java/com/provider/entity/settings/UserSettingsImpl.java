package com.provider.entity.settings;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class UserSettingsImpl implements UserSettings {
    private String locale;
    private final String timezone;

    private UserSettingsImpl(@NotNull String locale, @NotNull String timezone) {
        this.locale = locale;
        this.timezone = timezone;
    }

    public static @NotNull UserSettingsImpl newInstance(@NotNull String locale, @NotNull String timezone) {
        return new UserSettingsImpl(locale, timezone);
    }

    @Override
    public void setLocale(@NotNull String locale) {
        this.locale = locale;
    }

    @Override
    public @NotNull String getLocale() {
        return locale;
    }

    @Override
    public @NotNull String getTimezone() {
        return timezone;
    }

    @Override
    public String toString() {
        return "UserSettingsImpl{" +
                "locale='" + locale + '\'' +
                ", timezone='" + timezone + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSettingsImpl that = (UserSettingsImpl) o;
        return Objects.equals(locale, that.locale) && Objects.equals(timezone, that.timezone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locale, timezone);
    }
}
