package com.provider.dao;

import com.provider.dao.exception.DBException;
import com.provider.entity.product.Subscription;

import java.util.List;

public abstract class SubscriptionDao extends EntityDao<Integer, Subscription> {
    /**
     * Returns list of subscriptions bound to user account with {@code userAccountId}
     * @param userAccountId user account id that is used when paying for subscriptions
     * @return {@code List<Subscription> } containing all subscriptions bought by this account
     * @throws IllegalArgumentException if {@code userAccountId <= 0}
     */
    public abstract List<Subscription> findSubscriptions(long userAccountId) throws DBException;
}
