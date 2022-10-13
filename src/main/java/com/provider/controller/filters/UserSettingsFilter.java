package com.provider.controller.filters;

import com.provider.constants.attributes.SessionAttributes;
import com.provider.constants.params.UserSettingsParams;
import com.provider.entity.settings.UserSettings;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Handles user settings for incoming requests
 */
@WebFilter(filterName = "UserSettingsFilter", urlPatterns = "/*")
public class UserSettingsFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(UserSettingsFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {
        logger.info("Filter {} init", this);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        logger.debug("doFilter: filter: {}", this);
        if (request instanceof final HttpServletRequest httpServletRequest) {
            setLocale(httpServletRequest);
        } else {
            logger.error("Request object {} cannot be cast to HttServletRequest", request);
        }
        chain.doFilter(request, response);
    }

    private void setLocale(HttpServletRequest httpServletRequest) {
        final String newLocale = httpServletRequest.getParameter(UserSettingsParams.LOCALE);
        if (newLocale != null) {
            final var userSettings = (UserSettings) httpServletRequest.getSession()
                    .getAttribute(SessionAttributes.USER_SETTINGS);
            userSettings.setLocale(newLocale);
        }
    }
}
