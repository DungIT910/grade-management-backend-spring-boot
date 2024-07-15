package com.boolfly.grademanagementrestful.builder.base;

import com.boolfly.grademanagementrestful.builder.entitypathprovider.base.CommonEntityPathProvider;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.hypersistence.tsid.TSID;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class AbstractSearchParamsBuilder implements SearchParamsBuilder {
    private final int page;
    private final int size;

    protected <T extends AbstractBuilder<T, B>, B extends AbstractSearchParamsBuilder>
    AbstractSearchParamsBuilder(AbstractBuilder<T, B> builder) {
        this.page = builder.page;
        this.size = builder.size;
    }

    /**
     * @param provider
     * @param name
     * @param id
     * @param status
     * @param <T>      status type
     * @return
     */
    protected <T> Optional<BooleanExpression> getCommonCriteria(CommonEntityPathProvider<T> provider, String name,
                                                                String id, List<T> status) {
        return Stream.of(
                        Optional.ofNullable(name)
                                .map(str -> "%" + str + "%")
                                .map(provider::nameLikeIgnoreCase),
                        Optional.ofNullable(id)
                                .map(TSID::from)
                                .map(TSID::toLong)
                                .map(provider::idEquals),
                        Optional.of(status)
                                .filter(stt -> !stt.isEmpty())
                                .map(provider::statusIn)
                )
                .filter(Optional::isPresent).map(Optional::get)
                .reduce(BooleanExpression::and);
    }

    @Override
    public Pageable getPageable() {
        return PageRequest.of(page, size);
    }

    /**
     * @param <T> subclass of AbstractBuilder
     * @param <B> subclass of AbstractSearchParamsBuilder
     */
    @SuppressWarnings("unchecked")
    protected abstract static class AbstractBuilder<T extends AbstractBuilder<T, B>, B extends AbstractSearchParamsBuilder> {
        protected int page;
        protected int size;

        public T withPage(int page) {
            this.page = page;
            return (T) this;
        }

        public T withSize(int size) {
            this.size = size;
            return (T) this;
        }

        public abstract B build();
    }
}
