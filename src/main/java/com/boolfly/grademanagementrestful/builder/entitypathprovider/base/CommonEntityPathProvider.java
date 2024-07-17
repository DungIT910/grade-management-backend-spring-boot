package com.boolfly.grademanagementrestful.builder.entitypathprovider.base;

import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;

/**
 * Interface for providing common entity path expressions.
 *
 * @param <T> The entity status type
 * @author Bao Le
 */
public interface CommonEntityPathProvider<T> {
    /**
     * Creates a {@link BooleanExpression} that checks if the entity's name matches the given name, ignoring case.
     *
     * @param name the name to be matched
     * @return a {@link BooleanExpression} for the name match
     */
    BooleanExpression nameLikeIgnoreCase(String name);

    /**
     * Creates a {@link BooleanExpression} that checks if the entity's ID equals the given ID.
     *
     * @param id the ID to be matched
     * @return a {@link BooleanExpression} for the ID match
     */
    BooleanExpression idEquals(Long id);

    /**
     * Creates a {@link BooleanExpression} that checks if the entity's status is in the given list of statuses.
     *
     * @param status the list of statuses to be matched
     * @return a {@link BooleanExpression} for the status match
     */
    BooleanExpression statusIn(List<T> status);
}
