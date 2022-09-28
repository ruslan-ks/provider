package com.provider.controller.listeners;

import com.provider.constants.attributes.AppAttributes;
import com.provider.service.ServiceFactory;
import com.provider.service.ServiceFactoryImpl;
import com.provider.service.UserService;
import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import com.provider.entity.user.impl.UserImpl;
import com.provider.entity.user.UserPassword;
import com.provider.localization.LanguageInfo;
import com.provider.localization.MapBasedLanguageInfo;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Sets application initial attributes used by its parts
 */
@WebListener
public class AppListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(AppListener.class);

    public AppListener() {}

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        setAppScopeAttributes(servletContextEvent.getServletContext());
        logger.info("Application context initialized successfully");

        tryCreateRootUser();
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

    public void tryCreateRootUser() {
        try {
            // TODO: fix this: root object doesn't get updated if root user already exists in db.
            User root = UserImpl.of(0, "root", "root", "root", "000000",
                    User.Role.ROOT, User.Status.ACTIVE);
            final ServiceFactory serviceFactory = ServiceFactoryImpl.newInstance();
            final UserService userService = serviceFactory.getUserService();
            final Optional<User> found = userService.findUserByLogin(root.getLogin());
            final UserPassword rootPassword;
            if (found.isEmpty()) {
                // Create new password and insert with new user
                rootPassword = UserPassword.hash("password");
                final boolean inserted = userService.insertUser(root, rootPassword);
                logger.info("Init: inserted root user: {}", inserted);
            } else {
                // Get existing user and password
                root = found.get();
                rootPassword = userService.findUserPassword(root.getId()).orElseThrow();
            }
            // TODO: delete this log
            logger.info("Root user: {}\nRoot user password: {}", root, rootPassword);
        } catch (DBException ex) {
            logger.error("Failed to create root user", ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        /* This method is called when the servlet Context is undeployed or Application Server shuts down. */
    }
}
