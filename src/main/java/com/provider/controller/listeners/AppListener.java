package com.provider.controller.listeners;

import com.provider.constants.attributes.AppAttributes;
import com.provider.service.*;
import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import com.provider.entity.user.impl.UserImpl;
import com.provider.service.exception.ValidationException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@WebListener
public class AppListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(AppListener.class);

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        setAppLocales(servletContextEvent.getServletContext());
        setTariffImagesUploadPath(servletContextEvent.getServletContext());

        tryCreateRootUser();
        tryCreateTestUsers();

        startSubscriptionRenewalExecutor();

        logger.info("Application context initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        scheduledExecutorService.shutdown();
    }

    private void startSubscriptionRenewalExecutor() {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            final SubscriptionService subscriptionService;
            try {
                logger.info("Trying to renew expired subscriptions...");
                subscriptionService = ServiceFactoryImpl.newInstance().getSubscriptionService();
                subscriptionService.renewAllExpiredActiveSubscriptions(
                        (s) -> logger.info("+++ Renewed subscription: {}", s),
                        (s) -> logger.warn("--- Cannot renew subscription! Not enough money! subscription: {}", s));
                logger.info("Subscription renewal finished.");
            } catch (DBException ex) {
                logger.error("Failed to renew subscriptions", ex);
                throw new RuntimeException(ex);
            }
        }, 1, 1, TimeUnit.MINUTES);
        logger.info("Started scheduled subscription renewal executor service");
    }

    private void setAppLocales(@NotNull ServletContext servletContext) {
        final String localesPropertiesFilePath = servletContext.getInitParameter("localesPropertiesFilePath");
        logger.info("Loading locales properties file {}", localesPropertiesFilePath);

        final Properties localeProperties = new Properties();
        try (var inputStream = servletContext.getResourceAsStream(localesPropertiesFilePath);
             var inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            localeProperties.load(inputStreamReader);
            servletContext.setAttribute(AppAttributes.LOCALE_LANG_MAP, localeProperties);
        } catch (IOException ex) {
            logger.error("Failed to load locales properties file", ex);
            throw new RuntimeException(ex);
        }
    }

    private void setTariffImagesUploadPath(@NotNull ServletContext servletContext) {
        final String tariffImageDir = "upload" + File.separator + "images";
        final String tariffImagesUploadPath = servletContext.getRealPath("") + File.separator + tariffImageDir;
        servletContext.setAttribute(AppAttributes.TARIFF_IMAGE_UPLOAD_PATH, tariffImagesUploadPath);
        servletContext.setAttribute(AppAttributes.TARIFF_IMAGE_DIR, tariffImageDir);
    }

    public void tryCreateRootUser() {
        try {
            // TODO: fix this: root object doesn't get updated if root user already exists in db.
            User root = UserImpl.of(0, "root", "root", "root", "000000",
                    User.Role.ROOT, User.Status.ACTIVE);
            final ServiceFactory serviceFactory = ServiceFactoryImpl.newInstance();
            final UserService userService = serviceFactory.getUserService();
            final Optional<User> found = userService.findUserByLogin(root.getLogin());
            if (found.isEmpty()) {
                // Create and insert new root user
                final boolean inserted = userService.insertUser(root, "password");
                logger.info("Init: inserted root user: {}", inserted);
            }
        } catch (DBException ex) {
            logger.error("Failed to create root user", ex);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    private void tryCreateTestUsers() {
        final ServiceFactory serviceFactory = ServiceFactoryImpl.newInstance();
        final UserService userService;
        try {
            userService = serviceFactory.getUserService();
        } catch (DBException ex) {
            throw new RuntimeException(ex);
        }

        final Map<User, String> testUsers = getTestUsers();
        for (var entry : testUsers.entrySet()) {
            try {
                final Optional<User> found = userService.findUserByLogin(entry.getKey().getLogin());
                if (found.isEmpty()) {
                    userService.insertUser(entry.getKey(), entry.getValue());
                }
                logger.info("Inserted test user: {}", entry.getKey());
            } catch (DBException ex) {
                logger.error("Failed to create test user: " + entry.getKey(), ex);
            } catch (ValidationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Map<User, String> getTestUsers() {
        return Stream.iterate(2, i -> ++i)
                .map(i -> UserImpl.of(0, "name" + i, "surname" + i, "user" + i,
                        String.valueOf(i).repeat(10).substring(0, 10), User.Role.MEMBER, User.Status.ACTIVE))
                .limit(10)
                .collect(Collectors.toMap(Function.identity(), u -> "pass"));
    }
}
