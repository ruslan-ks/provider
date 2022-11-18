package com.provider.controller.listeners;

import com.provider.constants.AppInitParams;
import com.provider.constants.attributes.AppAttributes;
import com.provider.service.*;
import com.provider.dao.exception.DBException;
import com.provider.service.impl.ServiceFactoryImpl;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class AppListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(AppListener.class);

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        setAppLocales(servletContextEvent.getServletContext());
        setTariffImagesUploadPath(servletContextEvent.getServletContext());

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
        //final String tariffImagesUploadPath = servletContext.getRealPath("") + File.separator + tariffImageDir;
        final String fileUploadDir = servletContext.getInitParameter(AppInitParams.FILE_UPLOAD_DIR);
        final Path imageUploadPath = Paths.get(fileUploadDir, "images");
        servletContext.setAttribute(AppAttributes.TARIFF_IMAGE_UPLOAD_DIR_PATH, imageUploadPath.toString());
    }
}
