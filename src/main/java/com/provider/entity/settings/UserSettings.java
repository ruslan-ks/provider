package com.provider.entity.settings;

import com.provider.entity.Entity;
import org.jetbrains.annotations.NotNull;

/**
 * Represents user settings.
 * Used to save user settings.
 */
public interface UserSettings extends Entity {
    void setLocale(@NotNull String locale);

    @SuppressWarnings("unused")
    @NotNull String getLocale();

    /**
     * Timezone. Example: 'GMT+3'
     * @return user timezone
     */
    @SuppressWarnings("unused")
    @NotNull String getTimezone();
}
