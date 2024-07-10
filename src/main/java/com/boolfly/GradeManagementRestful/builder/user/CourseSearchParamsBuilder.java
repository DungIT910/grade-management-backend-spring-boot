package com.boolfly.GradeManagementRestful.builder.user;

import com.boolfly.GradeManagementRestful.api.dto.course.SearchCourseRequest;
import com.boolfly.GradeManagementRestful.builder.base.AbstractSearchParamsBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.hypersistence.tsid.TSID;

import java.util.Optional;
import java.util.stream.Stream;

import static com.boolfly.GradeManagementRestful.domain.QCourse.course;

public final class CourseSearchParamsBuilder extends AbstractSearchParamsBuilder {
    private final String courseId;
    private final String name;


    private CourseSearchParamsBuilder(CourseBuilder builder) {
        super(builder);
        this.name = builder.name;
        this.courseId = builder.courseId;
    }

    public static CourseSearchParamsBuilder from(int page, int size, SearchCourseRequest request) {
        return new CourseBuilder()
                .withPage(page)
                .withSize(size)
                .withName(request.getName())
                .withCourseId(request.getCourseId())
                .build();
    }

    @Override
    public Optional<BooleanExpression> getCommonCriteria() {
        return Stream.of(
                        Optional.ofNullable(name)
                                .map(str -> "%" + str + "%")
                                .map(course.name::likeIgnoreCase),
                        Optional.ofNullable(courseId)
                                .map(TSID::from)
                                .map(TSID::toLong)
                                .map(course.id::eq)
                )
                .filter(Optional::isPresent).map(Optional::get)
                .reduce(BooleanExpression::and);
    }

    public static class CourseBuilder extends AbstractBuilder<CourseBuilder, CourseSearchParamsBuilder> {
        private String courseId;
        private String name;

        public CourseBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public CourseBuilder withCourseId(String courseId) {
            this.courseId = courseId;
            return this;
        }

        @Override
        public CourseSearchParamsBuilder build() {
            return new CourseSearchParamsBuilder(this);
        }
    }
}
