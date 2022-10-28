package com.provider.dao.postgres.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class PostgresQueryBuilder {
    private final String table;
    private boolean distinct;
    private final List<String> selectFields = new ArrayList<>();
    private final Map<String, String> leftJoins = new LinkedHashMap<>();
    private final Map<String, String> innerJoins = new LinkedHashMap<>();
    private final List<String> orderByFields = new ArrayList<>();
    private boolean limitArg;
    private boolean offsetArg;
    private String whereCondition = "true";

    protected PostgresQueryBuilder(@NotNull String table) {
        this.table = table;
    }

    public static PostgresQueryBuilder of(@NotNull String table) {
        return new PostgresQueryBuilder(table);
    }

    public @NotNull String getQuery() {
        final String selectPart = "SELECT " + (distinct ? "DISTINCT " : "") + String.join(", ", selectFields);
        final String fromPart = "FROM " + table;
        final String innerJoinsPart = innerJoins.entrySet().stream()
                .map(e -> "INNER JOIN " + e.getKey() + " ON " + e.getValue())
                .collect(Collectors.joining(" "));
        final String leftJoinsPart = leftJoins.entrySet().stream()
                .map(e -> "LEFT JOIN " + e.getKey() + " ON " + e.getValue())
                .collect(Collectors.joining(" "));
        final String wherePart = "WHERE " + whereCondition;
        final String orderByPart = !orderByFields.isEmpty()
                ? "ORDER BY " + String.join(", ", orderByFields)
                : "";
        final String offsetPart = offsetArg ? "OFFSET ?" : "";
        final String limitPart = limitArg ? "LIMIT ?" : "";
        return selectPart + " " + fromPart + " " + innerJoinsPart + " " + leftJoinsPart + " " + wherePart + " " +
                orderByPart + " " + offsetPart + " " + limitPart;
    }

    /**
     * Adds fields to be selected
     * Each field may contain alias, for example: {@code tariffs.id AS tariff_id}
     * @param fields fields to be selected
     * @param distinct defines whether {@code DISTINCT} keyword should be used or not
     * @return reference to the same builder object
     */
    public PostgresQueryBuilder addSelect(@NotNull List<String> fields, boolean distinct) {
        selectFields.addAll(fields);
        this.distinct = distinct;
        return this;
    }

    /**
     * Adds tables to be joined with left join
     * @param tableName table name
     * @param condition condition
     * @return reference to the same builder object
     */
    public PostgresQueryBuilder addLeftJoin(@NotNull String tableName, @NotNull String condition) {
        leftJoins.put(tableName, condition);
        return this;
    }

    /**
     * Adds tables to be joined with inner join
     * @param tableName table name
     * @param condition condition
     * @return reference to the same builder object
     */
    public PostgresQueryBuilder addInnerJoin(@NotNull String tableName, @NotNull String condition) {
        innerJoins.put(tableName, condition);
        return this;
    }

    /**
     * Sets WHERE condition of the query
     * @param condition condition that will be placed after WHERE keyword; may contain AND, OR operators
     * @return this reference
     */
    public PostgresQueryBuilder setWhere(@NotNull String condition) {
        if (!condition.isBlank()) {
            this.whereCondition = condition;
        }
        return this;
    }

    /**
     * Sets offset argument.
     * If {@code offsetArg} is true, resulting query will have {@code offset ?}
     * @return the same builder object reference
     */
    public PostgresQueryBuilder setOffsetArg(boolean offsetArg) {
        this.offsetArg = offsetArg;
        return this;
    }

    /**
     * Sets limit argument.
     * If {@code limitArg} is true, resulting query will have {@code limit ?}
     * @return the same builder object reference
     */
    public PostgresQueryBuilder setLimitArg(boolean limitArg) {
        this.limitArg = limitArg;
        return this;
    }

    /**
     * Adds ORDER BY fields.
     * ORDER BY fields are appended to the query in the order of {@code addOrderByFields} calls
     * @param orderByFields fields to be used with ORDER BY clause; may contain DESC keyword
     * @return reference to the same builder object
     */
    public PostgresQueryBuilder addOrderBy(@NotNull List<String> orderByFields) {
        this.orderByFields.addAll(orderByFields);
        return this;
    }
}
