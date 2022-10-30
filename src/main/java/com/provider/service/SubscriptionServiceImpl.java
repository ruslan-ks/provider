package com.provider.service;

import com.provider.dao.SubscriptionDao;
import com.provider.dao.UserAccountDao;
import com.provider.dao.exception.DBException;
import com.provider.dao.transaction.Transaction;
import com.provider.entity.dto.SubscriptionDto;
import com.provider.entity.dto.SubscriptionTariffDto;
import com.provider.entity.product.Subscription;
import com.provider.entity.product.Tariff;
import com.provider.entity.product.TariffDuration;
import com.provider.entity.user.UserAccount;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.*;
import java.util.List;
import java.util.function.Consumer;

public class SubscriptionServiceImpl extends AbstractService implements SubscriptionService {
    private static final Logger logger = LoggerFactory.getLogger(SubscriptionServiceImpl.class);

    protected SubscriptionServiceImpl() throws DBException {}

    @Override
    public boolean buySubscription(@NotNull UserAccount userAccount, @NotNull Tariff tariff) throws DBException {
        final SubscriptionDao subscriptionDao = daoFactory.newSubscriptionDao();
        final UserAccountDao userAccountDao = daoFactory.newUserAccountDao();
        try (var transaction = Transaction.of(connectionSupplier.get(), subscriptionDao, userAccountDao)) {
            try {
                final boolean isAlreadySubscribed = activeSubscriptionExists(subscriptionDao, userAccount, tariff);
                if (isAlreadySubscribed || !hasEnoughMoneyToPay(userAccount, tariff)) {
                    return false;
                }

                final Instant currentTime = Instant.now();
                final Subscription subscription = entityFactory.newSubscription(0, userAccount.getId(),
                        tariff.getId(), currentTime, currentTime, Subscription.Status.ACTIVE);
                final boolean subscriptionInserted = subscriptionDao.insert(subscription);

                userAccount.withdraw(tariff.getUsdPrice());
                final boolean userAccountUpdated = userAccountDao.update(userAccount);

                if (subscriptionInserted && userAccountUpdated) {
                    transaction.commit();
                    return true;
                }
            } catch (Throwable ex) {
                logger.error("Failed to execute transaction: {}", transaction);
                logger.error("Failed to buy subscription: user account: {}, tariff: {}", userAccount, tariff);
                logger.error("Failed to execute transaction!", ex);
                transaction.rollback();
                throw ex;
            }
            transaction.rollback();
        }
        return false;
    }

    @Override
    public boolean hasEnoughMoneyToPay(@NotNull UserAccount userAccount, @NotNull Tariff tariff) {
        return userAccount.getAmount().compareTo(tariff.getUsdPrice()) >= 0;
    }

    @Override
    public boolean activeSubscriptionExists(@NotNull UserAccount userAccount, @NotNull Tariff tariff) throws DBException {
        final SubscriptionDao subscriptionDao = daoFactory.newSubscriptionDao();
        try (var connection = connectionSupplier.get()) {
            subscriptionDao.setConnection(connection);
            return activeSubscriptionExists(subscriptionDao, userAccount, tariff);
        } catch (SQLException ex) {
            logger.error("Failed to close connection!", ex);
            throw new DBException(ex);
        }
    }

    private boolean activeSubscriptionExists(@NotNull SubscriptionDao subscriptionDao, @NotNull UserAccount userAccount,
                                         @NotNull Tariff tariff) throws DBException {
        return subscriptionDao.findSubscriptions(userAccount.getId()).stream()
                .filter(this::hasActiveStatus)
                .map(Subscription::getTariffId)
                .anyMatch(id -> id == tariff.getId());
    }

    private boolean hasActiveStatus(@NotNull Subscription subscription) {
        return (subscription.getStatus() == Subscription.Status.ACTIVE);
    }

    private boolean hasActiveStatus(@NotNull SubscriptionTariffDto subscriptionTariffDto) {
        return hasActiveStatus(subscriptionTariffDto.getSubscription());
    }

    @Override
    public @NotNull List<Subscription> findActiveSubscriptions(@NotNull UserAccount userAccount) throws DBException {
        final SubscriptionDao subscriptionDao = daoFactory.newSubscriptionDao();
        try (var connection = connectionSupplier.get()) {
            subscriptionDao.setConnection(connection);
            return subscriptionDao.findSubscriptions(userAccount.getId())
                    .stream()
                    .filter(this::hasActiveStatus)
                    .toList();
        } catch (SQLException ex) {
            logger.error("Failed to close connection!", ex);
            throw new DBException(ex);
        }
    }

    @Override
    public @NotNull List<SubscriptionTariffDto> findActiveSubscriptionsFullInfo(@NotNull UserAccount userAccount,
                                                                                @NotNull String locale)
            throws DBException {
        final SubscriptionDao subscriptionDao = daoFactory.newSubscriptionDao();
        try (var connection = connectionSupplier.get()) {
            subscriptionDao.setConnection(connection);
            return subscriptionDao.findSubscriptionsFullInfo(userAccount.getId(), locale)
                    .stream()
                    .filter(this::hasActiveStatus)
                    .toList();
        } catch (SQLException ex) {
            logger.error("Failed to close connection!", ex);
            throw new DBException(ex);
        }
    }

    @Override
    public @NotNull Instant computeNextPaymentTime(@NotNull Subscription subscription,
                                                   @NotNull TariffDuration tariffDuration) {
        if (subscription.getTariffId() != tariffDuration.getTariffId()) {
            throw new IllegalArgumentException("Subscription tariff id(" + subscription.getTariffId() + ") != " +
                    "tariff duration tariff id(" + tariffDuration.getTariffId() + ")");
        }
        return ZonedDateTime.ofInstant(subscription.getLastPaymentTime(), ZoneOffset.UTC)
                .plus(Period.ofMonths(tariffDuration.getMonths()))
                .plus(Duration.ofMinutes(tariffDuration.getMinutes()))
                .toInstant();
    }

    @Override
    public boolean unsubscribe(@NotNull Subscription subscription) throws DBException {
        try (var connection = connectionSupplier.get()) {
            final SubscriptionDao subscriptionDao = daoFactory.newSubscriptionDao();
            subscriptionDao.setConnection(connection);
            subscription.setStatus(Subscription.Status.INACTIVE);
            return subscriptionDao.update(subscription);
        } catch (SQLException  ex) {
            logger.error("Failed to close connection", ex);
            throw new DBException(ex);
        }
    }

    @Override
    public List<SubscriptionDto> findAllExpiredActiveSubscriptions() throws DBException {
        try (var connection = connectionSupplier.get()) {
            final SubscriptionDao subscriptionDao = daoFactory.newSubscriptionDao();
            subscriptionDao.setConnection(connection);
            return subscriptionDao.findAllExpiredActiveSubscriptions();
        } catch (SQLException  ex) {
            logger.error("Failed to close connection", ex);
            throw new DBException(ex);
        }
    }

    @Override
    public void renewAllExpiredActiveSubscriptions(@NotNull Consumer<SubscriptionDto> renewedConsumer,
                                                   @NotNull Consumer<SubscriptionDto> notEnoughMoneyConsumer)
            throws DBException {
        final List<SubscriptionDto> expiredSubscriptionDtoList = findAllExpiredActiveSubscriptions();
        final SubscriptionDao subscriptionDao = daoFactory.newSubscriptionDao();
        final UserAccountDao userAccountDao = daoFactory.newUserAccountDao();
        for (var subscriptionDto : expiredSubscriptionDtoList) {
            final Subscription subscription = subscriptionDto.getSubscription();
            final Tariff tariff = subscriptionDto.getTariff();
            try (var transaction = Transaction.of(connectionSupplier.get(), subscriptionDao,
                    userAccountDao)) {
                try {
                    final UserAccount userAccount = userAccountDao.findByKey(subscription.getUserAccountId())
                            .orElseThrow();
                    if (hasEnoughMoneyToPay(userAccount, tariff)) {
                        userAccount.withdraw(tariff.getUsdPrice());
                        subscription.setLastPaymentTime(Instant.now());
                        final boolean subscriptionUpdated = subscriptionDao.update(subscription);
                        final boolean accountUpdated = userAccountDao.update(userAccount);
                        if (subscriptionUpdated && accountUpdated) {
                            transaction.commit();
                            renewedConsumer.accept(subscriptionDto);
                        } else {
                            transaction.rollback();
                        }
                    } else {
                        notEnoughMoneyConsumer.accept(subscriptionDto);
                        // rollback?
                    }
                } catch (Throwable ex) {
                    logger.error("Failed to execute transaction: {}", transaction);
                    logger.error("Failed to execute transaction!", ex);
                    transaction.rollback();
                    throw ex;
                }
            }
        }
    }
}
