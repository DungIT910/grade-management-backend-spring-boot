package com.boolfly.grademanagementrestful.builder.subcol;

import com.boolfly.grademanagementrestful.api.dto.subcol.SearchSubcolRequest;
import com.boolfly.grademanagementrestful.builder.base.AbstractSearchParamsBuilder;
import com.boolfly.grademanagementrestful.builder.entitypathprovider.subcol.SubcolEntityPathProvider;
import com.boolfly.grademanagementrestful.domain.model.subcol.SubcolStatus;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static com.boolfly.grademanagementrestful.domain.QSubcol.subcol;

public final class SubcolSearchParamsBuilder extends AbstractSearchParamsBuilder {
    private final String subcolId;
    private final String name;
    private final String courseId;
    private final List<SubcolStatus> status;

    public SubcolSearchParamsBuilder(SubcolBuilder builder) {
        super(builder);
        this.subcolId = builder.subcolId;
        this.name = builder.name;
        this.courseId = builder.courseId;
        this.status = builder.status;
    }

    public static SubcolSearchParamsBuilder from(int page, int size, SearchSubcolRequest request) {
        return new SubcolBuilder()
                .withPage(page)
                .withSize(size)
                .withSubcolId(request.getSubcolId())
                .withCourseId(request.getCourseId())
                .withName(request.getName())
                .withStatus(request.getStatus())
                .build();
    }

    @Override
    public Optional<BooleanExpression> getCommonCriteria() {
        return Stream.of(
                        getCommonCriteria(SubcolEntityPathProvider.getInstance(), name, subcolId, status),
                        Optional.ofNullable(courseId)
                                .map(this::toTSIDLong)
                                .map(subcol.course.id::eq)
                )
                .filter(Optional::isPresent).map(Optional::get)
                .reduce(BooleanExpression::and);
    }

    public static class SubcolBuilder extends AbstractBuilder<SubcolBuilder, SubcolSearchParamsBuilder> {
        private String subcolId;
        private String name;
        private String courseId;
        private List<SubcolStatus> status;

        public SubcolBuilder withSubcolId(String subcolId) {
            this.subcolId = subcolId;
            return this;
        }

        public SubcolBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public SubcolBuilder withCourseId(String courseId) {
            this.courseId = courseId;
            return this;
        }

        public SubcolBuilder withStatus(List<SubcolStatus> status) {
            this.status = Objects.requireNonNullElseGet(status, List::of);
            return this;
        }

        @Override
        public SubcolSearchParamsBuilder build() {
            return new SubcolSearchParamsBuilder(this);
        }
    }
}
