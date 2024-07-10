package com.boolfly.GradeManagementRestful.builder.base;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public abstract class AbstractSearchParamsBuilder implements SearchParamsBuilder {
    private final int page;
    private final int size;

    protected <T extends AbstractBuilder<T, B>, B extends AbstractSearchParamsBuilder>
    AbstractSearchParamsBuilder(AbstractBuilder<T, B> builder) {
        this.page = builder.page;
        this.size = builder.size;
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
