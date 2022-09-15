package com.provider.entity.settings;

import com.provider.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Represents user specified settings.
 * Used to save user specified settings to the session.
 */
public interface UserSettings extends Entity {
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
