package com.boolfly.grademanagementrestful.builder.entitypathprovider.subject;

import com.boolfly.grademanagementrestful.builder.entitypathprovider.base.CommonEntityPathProvider;
import com.boolfly.grademanagementrestful.domain.model.subject.SubjectStatus;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;

import static com.boolfly.grademanagementrestful.domain.QSubject.subject;

public final class SubjectEntityPathProvider implements CommonEntityPathProvider<SubjectStatus> {
    private SubjectEntityPathProvider() {
        // private constructor
    }

    public static CommonEntityPathProvider<SubjectStatus> getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public BooleanExpression nameLikeIgnoreCase(String name) {
        return subject.name.likeIgnoreCase(name);
    }

    @Override
    public BooleanExpression idEquals(Long id) {
        return subject.id.eq(id);
    }

    @Override
    public BooleanExpression statusIn(List<SubjectStatus> status) {
        return subject.status.in(status);
    }

    private static class Holder {
        private static final CommonEntityPathProvider<SubjectStatus> INSTANCE = new SubjectEntityPathProvider();
    }
}
