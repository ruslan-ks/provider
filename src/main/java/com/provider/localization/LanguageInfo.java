package com.provider.localization;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Set;

/**
 * Contains supported languages and additional info needed for localization.
 */
public interface LanguageInfo {
    /**
     * @return unmodifiable set of short names of supported languages, for example: set of (en, uk)
     */
    @NotNull Set<String> getSupportedLanguages();

    /**
     * Returns appropriate locale for one of supported languages
     * @param language on of supported languages obtained via getSupportedLanguages()
     * @return locale object to be used with internationalization code
     * @throws IllegalArgumentException if language is not supported
     */
    @NotNull Locale getLocale(@NotNull String language);

    /**
     * @return the first one of supported languages; this depends on Set implementation
     * @throws java.util.NoSuchElementException if supported languages set is empty
     */
    @NotNull String getDefaultLanguage();
}
