package com.boolfly.grademanagementrestful.builder.entitypathprovider.course;

import com.boolfly.grademanagementrestful.builder.entitypathprovider.base.CommonEntityPathProvider;
import com.boolfly.grademanagementrestful.domain.model.course.CourseStatus;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;

import static com.boolfly.grademanagementrestful.domain.QCourse.course;

public final class CourseEntityPathProvider implements CommonEntityPathProvider<CourseStatus> {
    private CourseEntityPathProvider() {
        // private constructor
    }

    private static class Holder {
        private static final CommonEntityPathProvider<CourseStatus> INSTANCE = new CourseEntityPathProvider();
    }

    public static CommonEntityPathProvider<CourseStatus> getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public BooleanExpression nameLikeIgnoreCase(String name) {
        return course.name.likeIgnoreCase(name);
    }

    @Override
    public BooleanExpression idEquals(Long id) {
        return course.id.eq(id);
    }

    @Override
    public BooleanExpression statusIn(List<CourseStatus> status) {
        return course.status.in(status);
    }
}
