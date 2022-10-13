package com.provider.entity.settings;

import org.jetbrains.annotations.NotNull;

public class UserSettingsImpl implements UserSettings {
    private String locale;

    private UserSettingsImpl() {}

    public static @NotNull UserSettingsImpl newInstance() {
        return new UserSettingsImpl();
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
    public String toString() {
        return "SimpleUserSettings{locale='" + locale + "'}";
    }
}
