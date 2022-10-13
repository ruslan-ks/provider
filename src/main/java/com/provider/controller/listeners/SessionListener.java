package com.provider.controller.listeners;

import com.provider.constants.attributes.AppAttributes;
import com.provider.constants.attributes.SessionAttributes;
import com.provider.entity.settings.UserSettings;
import com.provider.entity.settings.UserSettingsImpl;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.util.Map;

@WebListener
public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        @SuppressWarnings("unchecked")
        final var localeLanguageMap = (Map<String, String>) se.getSession().getServletContext()
                .getAttribute(AppAttributes.LOCALE_LANG_MAP);
        final String defaultLocale = localeLanguageMap.entrySet().iterator().next().getKey();

        final UserSettings userSettings = UserSettingsImpl.newInstance();
        userSettings.setLocale(defaultLocale);
        se.getSession().setAttribute(SessionAttributes.USER_SETTINGS, userSettings);
    }
}
