package com.boolfly.grademanagementrestful.builder.base;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Interface for building search parameters.
 * Provides methods to get common criteria and pageable information.
 *
 * @author Bao Le
 */
public interface SearchParamsBuilder {
    /**
     * Retrieves the common criteria for the search.
     *
     * @return an {@link Optional} containing the {@link BooleanExpression} if present, otherwise an empty {@link Optional}
     */
    Optional<BooleanExpression> getCommonCriteria();

    /**
     * Retrieves the pageable information for the search.
     *
     * @return a {@link Pageable} object containing pagination information
     */
    Pageable getPageable();

    /**
     * Retrieves the value of the common criteria.
     * If the common criteria is not present, returns {@code null}.
     *
     * @return the {@link BooleanExpression} if present, otherwise {@code null}
     */
    default BooleanExpression getCommonCriteriaValue() {
        return getCommonCriteria().orElse(null);
    }
}
