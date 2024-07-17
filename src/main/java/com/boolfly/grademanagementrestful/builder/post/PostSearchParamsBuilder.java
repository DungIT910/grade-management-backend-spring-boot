package com.boolfly.grademanagementrestful.builder.post;

import com.boolfly.grademanagementrestful.api.dto.post.SearchPostRequest;
import com.boolfly.grademanagementrestful.builder.base.AbstractSearchParamsBuilder;
import com.boolfly.grademanagementrestful.builder.entitypathprovider.post.PostEntityPathProvider;
import com.boolfly.grademanagementrestful.domain.model.post.PostStatus;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static com.boolfly.grademanagementrestful.domain.QPost.post;

public class PostSearchParamsBuilder extends AbstractSearchParamsBuilder {
    private final String postId;
    private final String title;
    private final String forumId;
    private final String userId;
    private final List<PostStatus> status;

    public PostSearchParamsBuilder(PostBuilder builder) {
        super(builder);
        this.postId = builder.postId;
        this.title = builder.title;
        this.forumId = builder.forumId;
        this.userId = builder.userId;
        this.status = builder.status;
    }

    public static PostSearchParamsBuilder from(int page, int size, SearchPostRequest request) {
        return new PostSearchParamsBuilder.PostBuilder()
                .withPage(page)
                .withSize(size)
                .withPostId(request.getPostId())
                .withTitle(request.getTitle())
                .withForumId(request.getForumId())
                .withUserId(request.getUserId())
                .withStatus(request.getStatus())
                .build();
    }

    @Override
    public Optional<BooleanExpression> getCommonCriteria() {
        return Stream.of(
                        getCommonCriteria(PostEntityPathProvider.getInstance(), title, postId, status),
                        Optional.ofNullable(forumId)
                                .map(this::toTSIDLong)
                                .map(post.forum.id::eq),
                        Optional.ofNullable(userId)
                                .map(this::toTSIDLong)
                                .map(post.user.id::eq)
                )
                .filter(Optional::isPresent).map(Optional::get)
                .reduce(BooleanExpression::and);
    }

    public static class PostBuilder extends AbstractBuilder<PostBuilder, PostSearchParamsBuilder> {
        private String postId;
        private String title;
        private String forumId;
        private String userId;
        private List<PostStatus> status;

        public PostBuilder withPostId(String postId) {
            this.postId = postId;
            return this;
        }

        public PostBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public PostBuilder withForumId(String forumId) {
            this.forumId = forumId;
            return this;
        }

        public PostBuilder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public PostBuilder withStatus(List<PostStatus> status) {
            this.status = Objects.requireNonNullElseGet(status, List::of);
            return this;
        }

        @Override
        public PostSearchParamsBuilder build() {
            return new PostSearchParamsBuilder(this);
        }
    }
}
