package com.provider.settings;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class SimpleUserSettings implements UserSettings {
    private String language;

    private SimpleUserSettings() {}

    public static @NotNull SimpleUserSettings newInstance() {
        return new SimpleUserSettings();
    }

    @Override
    public void setLanguage(@NotNull String language) {
        this.language = language;
    }

    @Override
    public @NotNull Optional<String> getLanguage() {
        return Optional.ofNullable(language);
    }

    @Override
    public String toString() {
        return "SimpleUserSettings{language='" + language + "'}";
    }
}
