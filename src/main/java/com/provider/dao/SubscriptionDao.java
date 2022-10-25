package com.provider.dao;

import com.provider.dao.exception.DBException;
import com.provider.entity.dto.SubscriptionTariffDto;
import com.provider.entity.product.Subscription;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class SubscriptionDao extends EntityDao<Integer, Subscription> {
    /**
     * Returns list of subscriptions bound to user account with {@code userAccountId}
     * @param userAccountId user account id that is used when paying for subscriptions
     * @return {@code List<Subscription> } containing all subscriptions bought by this account
     * @throws DBException if {@code SQLException} is thrown
     * @throws IllegalArgumentException if {@code userAccountId <= 0}
     */
    public abstract List<Subscription> findSubscriptions(long userAccountId) throws DBException;

    /**
     * Returns {@code List<SubscriptionTariffDto} containing user subscriptions tariffs data<br>
     * If there is no translation for desired {@code locale}, the default content version will be returned
     * @param userAccountId user account bound to subscriptions id
     * @param locale desired locale
     * @return {@code List<SubscriptionTariffDto} containing user subscriptions tariffs data
     * @throws DBException if {@code SQLException} is thrown
     * @throws IllegalArgumentException if {@code userAccountId <= 0}
     */
    public abstract List<SubscriptionTariffDto> findSubscriptionsFullInfo(long userAccountId, @NotNull String locale)
            throws DBException;

    /**
     * Updates ONLY subscription's lastPaymentTime and status.<br>
     * userAccountId, tariffId, startTime are NOT UPDATED.
     * @param subscription subscription to be updated
     * @return true if db changes were made
     * @throws DBException if SQLException caught
     * @throws IllegalArgumentException if {@code subscription.getId() <= 0}
     */
    public abstract boolean update(@NotNull Subscription subscription) throws DBException;
}
