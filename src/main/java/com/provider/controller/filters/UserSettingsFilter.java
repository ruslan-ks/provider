package com.provider.controller.filters;

import com.provider.constants.attributes.AppAttributes;
import com.provider.constants.attributes.RequestAttributes;
import com.provider.constants.attributes.SessionAttributes;
import com.provider.constants.params.UserSettingsParams;
import com.provider.localization.LanguageInfo;
import com.provider.entity.settings.UserSettingsImpl;
import com.provider.entity.settings.UserSettings;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Locale;

/**
 * Handles user settings for incoming requests
 */
@WebFilter(filterName = "UserSettingsFilter", urlPatterns = "/*")
public class UserSettingsFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(UserSettingsFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("{}::init", this);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        logger.debug("doFilter: filter: {}", this);
        if (request instanceof HttpServletRequest) {
            final var httpServletRequest = (HttpServletRequest) request;
            setLocalizationSettingsAttributes(httpServletRequest);
        } else {
            logger.error("Request object {} cannot be cast to HttServletRequest", request);
        }
        chain.doFilter(request, response);
    }

    private void setLocalizationSettingsAttributes(HttpServletRequest httpServletRequest) {
        setUserSettingsAttribute(httpServletRequest);
        setLocaleAttribute(httpServletRequest);
    }

    /**
     * Creates and sets UserSettings as session scope attribute
     */
    private void setUserSettingsAttribute(HttpServletRequest request) {
        final HttpSession session = request.getSession();
        final String userChosenLanguage = request.getParameter(UserSettingsParams.LANGUAGE);
        final UserSettings userSettings = UserSettingsImpl.newInstance();
        boolean settingsChanged = false;
        if (userChosenLanguage != null) {
            userSettings.setLanguage(userChosenLanguage);
            settingsChanged = true;
            logger.debug("Setting user language: {} on settings {}", userChosenLanguage, userSettings);
        }

        // if session changed or there is no userSettings in the session
        if (settingsChanged || session.getAttribute(SessionAttributes.USER_SETTINGS) == null) {
            logger.debug("Setting user settings {} on session {}", userSettings, request.getSession());
            session.setAttribute(SessionAttributes.USER_SETTINGS, userSettings);
        }
    }

    /**
     * Sets user locale as a request attribute
     */
    private void setLocaleAttribute(HttpServletRequest request) {
        final var userSettings = (UserSettings) request.getSession().getAttribute(SessionAttributes.USER_SETTINGS);
        final var languageInfo = (LanguageInfo) request.getServletContext().getAttribute(AppAttributes.LANGUAGE_INFO);
        if (userSettings != null) {
            final String language = userSettings.getLanguage().orElseGet(languageInfo::getDefaultLanguage);
            final Locale locale = languageInfo.getLocale(language);
            request.setAttribute(RequestAttributes.LOCALE, locale);
        } else {
            logger.error("User settings {} not found in session scope", SessionAttributes.USER_SETTINGS);
            setDefaultLocale(request, languageInfo);
        }
    }

    private void setDefaultLocale(HttpServletRequest request, LanguageInfo languageInfo) {
        final String language = languageInfo.getDefaultLanguage();
        final Locale locale = languageInfo.getLocale(language);
        request.setAttribute(RequestAttributes.LOCALE, locale);
    }
}
