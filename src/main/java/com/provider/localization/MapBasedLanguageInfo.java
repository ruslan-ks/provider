package com.provider.localization;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Contains languages and corresponding locales. Based on Map where keys are languages and values are locales.
 */
public class MapBasedLanguageInfo implements LanguageInfo {
    private final Map<String, Locale> languageLocaleMap;

    private MapBasedLanguageInfo(@NotNull Map<String, Locale> languageLocaleMap) {
        this.languageLocaleMap = Map.copyOf(languageLocaleMap);
    }

    /**
     * Creates new instance
     * @param languageLocaleMap a map of supported languages and corresponding locales
     * @return new instance
     */
    public static @NotNull MapBasedLanguageInfo newInstance(@NotNull Map<String, Locale> languageLocaleMap) {
        return new MapBasedLanguageInfo(languageLocaleMap);
    }

    @Override
    public @NotNull Set<String> getSupportedLanguages() {
        return languageLocaleMap.keySet();
    }

    @Override
    public @NotNull Locale getLocale(@NotNull String language) {
        if (!languageLocaleMap.containsKey(language)) {
            throw new IllegalArgumentException("Language '" + language + "' not supported");
        }
        return languageLocaleMap.get(language);
    }

    @Override
    public @NotNull String getDefaultLanguage() {
        if (languageLocaleMap.isEmpty()) {
            throw new NoSuchElementException();
        }
        return languageLocaleMap.keySet().iterator().next();
    }
}
