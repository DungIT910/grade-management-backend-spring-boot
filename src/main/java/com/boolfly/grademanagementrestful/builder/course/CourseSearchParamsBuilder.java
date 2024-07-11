package com.boolfly.grademanagementrestful.builder.course;

import com.boolfly.grademanagementrestful.api.dto.course.SearchCourseRequest;
import com.boolfly.grademanagementrestful.builder.base.AbstractSearchParamsBuilder;
import com.boolfly.grademanagementrestful.builder.entitypathprovider.course.CourseEntityPathProvider;
import com.boolfly.grademanagementrestful.domain.model.course.CourseStatus;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class CourseSearchParamsBuilder extends AbstractSearchParamsBuilder {
    private final String courseId;
    private final String name;
    private final List<CourseStatus> status;


    private CourseSearchParamsBuilder(CourseBuilder builder) {
        super(builder);
        this.name = builder.name;
        this.courseId = builder.courseId;
        this.status = builder.status;
    }

    public static CourseSearchParamsBuilder from(int page, int size, SearchCourseRequest request) {
        return new CourseBuilder()
                .withPage(page)
                .withSize(size)
                .withName(request.getName())
                .withCourseId(request.getCourseId())
                .withStatus(request.getStatus())
                .build();
    }

    @Override
    public Optional<BooleanExpression> getCommonCriteria() {
        return getCommonCriteria(CourseEntityPathProvider.getInstance(), name, courseId, status);
    }

    public static class CourseBuilder extends AbstractBuilder<CourseBuilder, CourseSearchParamsBuilder> {
        private String courseId;
        private String name;
        private List<CourseStatus> status;

        public CourseBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public CourseBuilder withCourseId(String courseId) {
            this.courseId = courseId;
            return this;
        }

        public CourseBuilder withStatus(List<CourseStatus> status) {
            this.status = Objects.requireNonNullElseGet(status, List::of);
            return this;
        }

        @Override
        public CourseSearchParamsBuilder build() {
            return new CourseSearchParamsBuilder(this);
        }
    }
}
