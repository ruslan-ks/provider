package com.provider.settings;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Optional;

/**
 * Represents user specified settings.
 */
public interface UserSettings {
    /**
     * Set user chosen language
     * @param language language chosen by the user
     */
    void setLanguage(@NotNull String language);

    /**
     *
     * @return user chosen language if one was specified, otherwise Optional.empty()
     */
    @NotNull Optional<String> getLanguage();
}
