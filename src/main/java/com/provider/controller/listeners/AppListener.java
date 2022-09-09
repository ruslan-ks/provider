package com.provider.controller.listeners;

import com.provider.constants.attributes.AppAttributes;
import com.provider.localization.LanguageInfo;
import com.provider.localization.MapBasedLanguageInfo;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Map;

/**
 * Sets application initial attributes used by its parts
 */
@WebListener
public class AppListener implements ServletContextListener {
    private final Logger logger = LoggerFactory.getLogger(AppListener.class);

    public AppListener() {}

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        setAppScopeAttributes(servletContextEvent.getServletContext());
        logger.trace("Application context initialized");
    }

    private void setAppScopeAttributes(@NotNull ServletContext servletContext) {
        setAppLanguageInfoAttribute(servletContext);
    }

    private void setAppLanguageInfoAttribute(@NotNull ServletContext servletContext) {
        final Map<String, Locale> languageLocaleMap = Map.of("en", Locale.ENGLISH,
                "uk", new Locale("uk", "UA"));
        final LanguageInfo languageInfo = MapBasedLanguageInfo.newInstance(languageLocaleMap);
        servletContext.setAttribute(AppAttributes.LANGUAGE_INFO, languageInfo);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        /* This method is called when the servlet Context is undeployed or Application Server shuts down. */
    }
}
