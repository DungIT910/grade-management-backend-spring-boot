package com.boolfly.grademanagementrestful.builder.entitypathprovider.post;

import com.boolfly.grademanagementrestful.builder.entitypathprovider.base.CommonEntityPathProvider;
import com.boolfly.grademanagementrestful.domain.model.post.PostStatus;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;

import static com.boolfly.grademanagementrestful.domain.QPost.post;

public class PostEntityPathProvider implements CommonEntityPathProvider<PostStatus> {
    private PostEntityPathProvider() {
    }

    public static CommonEntityPathProvider<PostStatus> getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public BooleanExpression nameLikeIgnoreCase(String name) {
        return post.title.likeIgnoreCase(name);
    }

    @Override
    public BooleanExpression idEquals(Long id) {
        return post.id.eq(id);
    }

    @Override
    public BooleanExpression statusIn(List<PostStatus> status) {
        return post.status.in(status);
    }

    private static class Holder {
        private static final CommonEntityPathProvider<PostStatus> INSTANCE = new PostEntityPathProvider();
    }
}
