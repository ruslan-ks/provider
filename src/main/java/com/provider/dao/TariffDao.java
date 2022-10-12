package com.provider.dao;

import com.provider.dao.exception.DBException;
import com.provider.entity.dto.TariffDto;
import com.provider.entity.product.Tariff;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Tariff dao
 */
public abstract class TariffDao extends EntityDao<Integer, Tariff> {

    /**
     * Defines tariff records sorting order
     */
    public static class OrderRule {

        /**
         * Defines tariff records sorting by different fields
         */
        public enum OrderByField {
            ID,
            TITLE,
            STATUS,
            USD_PRICE
        }

        /**
         * Sort by id ascending
         */
        public static final OrderRule BY_ID = of(OrderByField.ID, false);

        /**
         * Sort by title ascending
         */
        public static final OrderRule BY_TITLE = of(OrderByField.TITLE, false);

        /**
         * Sort by title descending
         */
        public static final OrderRule BY_TITLE_DESC = of(OrderByField.TITLE, true);

        /**
         * Sort by price ascending
         */
        public static final OrderRule BY_PRICE = of(OrderByField.USD_PRICE, false);

        private final OrderByField orderByField;
        private final boolean desc;

        private OrderRule(@NotNull OrderByField orderByField, boolean desc) {
            this.orderByField = orderByField;
            this.desc = desc;
        }

        public static OrderRule of(@NotNull OrderByField orderByField, boolean desc) {
            return new OrderRule(orderByField, desc);
        }

        public OrderByField getOrderByField() {
            return orderByField;
        }

        public boolean isDesc() {
            return desc;
        }
    }

    public abstract @NotNull Optional<TariffDto> findFullInfoByKey(int id, @NotNull String language)
            throws DBException;
}
