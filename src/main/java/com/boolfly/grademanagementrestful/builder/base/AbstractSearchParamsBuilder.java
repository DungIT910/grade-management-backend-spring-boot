package com.boolfly.grademanagementrestful.builder.base;

import com.boolfly.grademanagementrestful.builder.entitypathprovider.base.CommonEntityPathProvider;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.hypersistence.tsid.TSID;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Abstract class for building search parameters.
 * Implements the {@link SearchParamsBuilder} interface and provides common functionality for search parameter builders.
 *
 * @author Bao Le
 * @see SearchParamsBuilder
 * @see AbstractBuilder
 */
public abstract class AbstractSearchParamsBuilder implements SearchParamsBuilder {
    private final int page;
    private final int size;

    /**
     * Constructs an instance of {@code AbstractSearchParamsBuilder} using the provided builder.
     *
     * @param builder the builder used to construct the instance
     * @param <T>     subclass of {@link AbstractBuilder}
     * @param <B>     subclass of {@link AbstractSearchParamsBuilder}
     */
    protected <T extends AbstractBuilder<T, B>, B extends AbstractSearchParamsBuilder>
    AbstractSearchParamsBuilder(AbstractBuilder<T, B> builder) {
        this.page = builder.page;
        this.size = builder.size;
    }

    /**
     * Retrieves the common criteria for the search based on the provided parameters.
     *
     * @param provider the provider for entity path
     * @param name     the name to be used in the criteria
     * @param id       the ID to be used in the criteria
     * @param status   the list of statuses to be used in the criteria
     * @param <T>      The entity status type
     * @return an {@link Optional} containing the {@link BooleanExpression} if present, otherwise an empty {@link Optional}
     */
    protected <T> Optional<BooleanExpression> getCommonCriteria(CommonEntityPathProvider<T> provider, String name,
                                                                String id, List<T> status) {
        return Stream.of(
                        Optional.ofNullable(name)
                                .map(str -> "%" + str + "%")
                                .map(provider::nameLikeIgnoreCase),
                        Optional.ofNullable(id)
                                .map(this::toTSIDLong)
                                .map(provider::idEquals),
                        Optional.of(status)
                                .filter(stt -> !stt.isEmpty())
                                .map(provider::statusIn)
                )
                .filter(Optional::isPresent).map(Optional::get)
                .reduce(BooleanExpression::and);
    }

    /**
     * Converts a string ID to a TSID long value.
     *
     * @param id the string ID to be converted
     * @return the TSID long value
     */
    protected Long toTSIDLong(String id) {
        return TSID.from(id).toLong();
    }

    /**
     * Retrieves the pageable information for the search.
     *
     * @return a {@link Pageable} object containing pagination information
     */
    @Override
    public Pageable getPageable() {
        return PageRequest.of(page, size);
    }

    /**
     * Abstract builder class for constructing instances of {@link AbstractSearchParamsBuilder}.
     *
     * @param <T> subclass of {@link AbstractBuilder}
     * @param <B> subclass of {@link AbstractSearchParamsBuilder}
     */
    @SuppressWarnings("unchecked")
    protected abstract static class AbstractBuilder<T extends AbstractBuilder<T, B>, B extends AbstractSearchParamsBuilder> {
        protected int page;
        protected int size;

        /**
         * Sets the page number for the builder.
         *
         * @param page the page number
         * @return the builder instance
         */
        public T withPage(int page) {
            this.page = page;
            return (T) this;
        }

        /**
         * Sets the page size for the builder.
         *
         * @param size the page size
         * @return the builder instance
         */
        public T withSize(int size) {
            this.size = size;
            return (T) this;
        }

        /**
         * Builds and returns an instance of a subclass of {@link AbstractSearchParamsBuilder}.
         *
         * @return an instance of a subclass of {@link AbstractSearchParamsBuilder}
         */
        public abstract B build();
    }
}
