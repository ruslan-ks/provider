package com.provider.localization;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;

class MapBasedLanguageInfoTest {
    @Test
    public void testGetSupportedLanguages() {
        final Map<String, Locale> languageLocaleMap = Map.of("en", Locale.ENGLISH,
                "uk", new Locale("uk", "UA"));
        final LanguageInfo languageInfo = MapBasedLanguageInfo.newInstance(languageLocaleMap);
        assertEquals(languageLocaleMap.keySet(), languageInfo.getSupportedLanguages());
    }

    @Test
    public void testGetLocale() {
        final Map<String, Locale> languageLocaleMap = Map.of("en", Locale.ENGLISH,
                "uk", new Locale("uk", "UA"));
        final LanguageInfo languageInfo = MapBasedLanguageInfo.newInstance(languageLocaleMap);
        languageLocaleMap.forEach((key, value) -> assertEquals(value, languageInfo.getLocale(key)));
    }

    @Test
    public void testGetLocaleThrows() {
        final Map<String, Locale> languageLocaleMap = Map.of("en", Locale.ENGLISH,
                "uk", new Locale("uk", "UA"));
        final LanguageInfo languageInfo = MapBasedLanguageInfo.newInstance(languageLocaleMap);
        assertThrows(IllegalArgumentException.class, () -> languageInfo.getLocale("fr"));
    }

    @Test
    public void testGetDefaultLanguageThrows() {
        final Map<String, Locale> languageLocaleMap = Collections.emptyMap();
        final LanguageInfo languageInfo = MapBasedLanguageInfo.newInstance(languageLocaleMap);
        assertThrows(NoSuchElementException.class, languageInfo::getDefaultLanguage);
    }
}