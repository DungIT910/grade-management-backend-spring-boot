package com.boolfly.grademanagementrestful.builder.entitypathprovider.forum;

import com.boolfly.grademanagementrestful.builder.entitypathprovider.base.CommonEntityPathProvider;
import com.boolfly.grademanagementrestful.domain.model.forum.ForumStatus;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;

import static com.boolfly.grademanagementrestful.domain.QForum.forum;

public class ForumEntityPathProvider implements CommonEntityPathProvider<ForumStatus> {
    private ForumEntityPathProvider() {
        // private constructor
    }

    public static CommonEntityPathProvider<ForumStatus> getInstance() {
        return ForumEntityPathProvider.Holder.INSTANCE;
    }

    @Override
    public BooleanExpression nameLikeIgnoreCase(String name) {
        return forum.name.likeIgnoreCase(name);
    }

    @Override
    public BooleanExpression idEquals(Long id) {
        return forum.id.eq(id);
    }

    @Override
    public BooleanExpression statusIn(List<ForumStatus> status) {
        return forum.status.in(status);
    }

    private static class Holder {
        private static final CommonEntityPathProvider<ForumStatus> INSTANCE = new ForumEntityPathProvider();
    }
}
