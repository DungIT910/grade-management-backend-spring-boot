package com.boolfly.GradeManagementRestful.builder.course;

import com.boolfly.GradeManagementRestful.api.dto.course.SearchCourseRequest;
import com.boolfly.GradeManagementRestful.builder.base.AbstractSearchParamsBuilder;
import com.boolfly.GradeManagementRestful.domain.model.course.CourseStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.hypersistence.tsid.TSID;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static com.boolfly.GradeManagementRestful.domain.QCourse.course;

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
        return Stream.of(
                        Optional.ofNullable(name)
                                .map(str -> "%" + str + "%")
                                .map(course.name::likeIgnoreCase),
                        Optional.ofNullable(courseId)
                                .map(TSID::from)
                                .map(TSID::toLong)
                                .map(course.id::eq),
                        Optional.of(status)
                                .filter(stt -> !stt.isEmpty())
                                .map(course.status::in)
                )
                .filter(Optional::isPresent).map(Optional::get)
                .reduce(BooleanExpression::and);
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
