package com.provider.controller.listeners;

import com.provider.constants.AppInitParams;
import com.provider.constants.attributes.AppAttributes;
import com.provider.constants.attributes.SessionAttributes;
import com.provider.entity.settings.UserSettings;
import com.provider.entity.settings.UserSettingsImpl;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.util.Map;

@WebListener
public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        final ServletContext context = se.getSession().getServletContext();
        @SuppressWarnings("unchecked")
        final var localeLanguageMap = (Map<String, String>) context.getAttribute(AppAttributes.LOCALE_LANG_MAP);
        final String defaultLocale = localeLanguageMap.entrySet().iterator().next().getKey();
        final String defaultTimezone = context.getInitParameter(AppInitParams.DEFAULT_USER_TIMEZONE);

        final UserSettings userSettings = UserSettingsImpl.newInstance(defaultLocale, defaultTimezone);
        se.getSession().setAttribute(SessionAttributes.USER_SETTINGS, userSettings);
    }
}
