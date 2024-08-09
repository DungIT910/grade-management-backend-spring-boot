package com.boolfly.grademanagementrestful.builder.grade;

import com.boolfly.grademanagementrestful.api.dto.grade.SearchGradeRequest;
import com.boolfly.grademanagementrestful.builder.base.AbstractSearchParamsBuilder;
import com.boolfly.grademanagementrestful.domain.model.maingrade.MaingradeStatus;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.Optional;
import java.util.stream.Stream;

import static com.boolfly.grademanagementrestful.domain.QMaingrade.maingrade;

public final class GradeSearchParamsBuilder extends AbstractSearchParamsBuilder {
    private final String courseId;
    private final String studentId;

    public GradeSearchParamsBuilder(GradeBuilder gradeBuilder) {
        super(gradeBuilder);
        this.courseId = gradeBuilder.courseId;
        this.studentId = gradeBuilder.studentId;
    }

    public static GradeSearchParamsBuilder from(int page, int size, SearchGradeRequest request) {
        return new GradeSearchParamsBuilder.GradeBuilder()
                .withStudentId(request.getStudentId())
                .withCourseId(request.getCourseId())
                .withPage(page)
                .withSize(size)
                .build();
    }

    @Override
    public Optional<BooleanExpression> getCommonCriteria() {
        return Stream.of(
                        Optional.ofNullable(courseId)
                                .map(this::toTSIDLong)
                                .map(maingrade.course.id::eq),
                        Optional.ofNullable(studentId)
                                .map(this::toTSIDLong)
                                .map(maingrade.student.id::eq),
                        Optional.of(maingrade.status.ne(MaingradeStatus.INACTIVE))
                ).filter(Optional::isPresent).map(Optional::get)
                .reduce(BooleanExpression::and);
    }

    public static class GradeBuilder extends AbstractBuilder<GradeBuilder, GradeSearchParamsBuilder> {
        private String courseId;
        private String studentId;

        public GradeBuilder withCourseId(String courseId) {
            this.courseId = courseId;
            return this;
        }

        public GradeBuilder withStudentId(String studentId) {
            this.studentId = studentId;
            return this;
        }

        @Override
        public GradeSearchParamsBuilder build() {
            return new GradeSearchParamsBuilder(this);
        }
    }
}
