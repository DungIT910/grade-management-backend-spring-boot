package com.boolfly.grademanagementrestful.builder.entitypathprovider.subcol;

import com.boolfly.grademanagementrestful.builder.entitypathprovider.base.CommonEntityPathProvider;
import com.boolfly.grademanagementrestful.domain.model.subcol.SubcolStatus;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;

import static com.boolfly.grademanagementrestful.domain.QSubcol.subcol;

public final class SubcolEntityPathProvider implements CommonEntityPathProvider<SubcolStatus> {
    private SubcolEntityPathProvider() {
        // private constructor
    }

    public static CommonEntityPathProvider<SubcolStatus> getInstance() {
        return SubcolEntityPathProvider.Holder.INSTANCE;
    }

    @Override
    public BooleanExpression nameLikeIgnoreCase(String name) {
        return subcol.name.likeIgnoreCase(name);
    }

    @Override
    public BooleanExpression idEquals(Long id) {
        return subcol.id.eq(id);
    }

    @Override
    public BooleanExpression statusIn(List<SubcolStatus> status) {
        return subcol.status.in(status);
    }

    private static class Holder {
        private static final CommonEntityPathProvider<SubcolStatus> INSTANCE = new SubcolEntityPathProvider();
    }
}
