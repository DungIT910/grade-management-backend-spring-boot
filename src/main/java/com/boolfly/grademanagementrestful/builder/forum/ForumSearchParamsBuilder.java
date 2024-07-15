package com.boolfly.grademanagementrestful.builder.forum;

import com.boolfly.grademanagementrestful.api.dto.forum.SearchForumRequest;
import com.boolfly.grademanagementrestful.builder.base.AbstractSearchParamsBuilder;
import com.boolfly.grademanagementrestful.builder.entitypathprovider.forum.ForumEntityPathProvider;
import com.boolfly.grademanagementrestful.domain.model.forum.ForumStatus;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class ForumSearchParamsBuilder extends AbstractSearchParamsBuilder {
    private final String forumId;
    private final String name;
    private final List<ForumStatus> status;

    public ForumSearchParamsBuilder(ForumBuilder builder) {
        super(builder);
        this.forumId = builder.forumId;
        this.name = builder.name;
        this.status = builder.status;
    }

    public static ForumSearchParamsBuilder from(int page, int size, SearchForumRequest request) {
        return new ForumBuilder()
                .withPage(page)
                .withSize(size)
                .withName(request.getName())
                .withForumId(request.getForumId())
                .withStatus(request.getStatus())
                .build();
    }

    @Override
    public Optional<BooleanExpression> getCommonCriteria() {
        return getCommonCriteria(ForumEntityPathProvider.getInstance(), name, forumId, status);
    }

    public static class ForumBuilder extends AbstractBuilder<ForumBuilder, ForumSearchParamsBuilder> {
        private String forumId;
        private String name;
        private List<ForumStatus> status;

        public ForumBuilder withForumId(String forumId) {
            this.forumId = forumId;
            return this;
        }

        public ForumBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ForumBuilder withStatus(List<ForumStatus> status) {
            this.status = Objects.requireNonNullElseGet(status, List::of);
            return this;
        }

        @Override
        public ForumSearchParamsBuilder build() {
            return new ForumSearchParamsBuilder(this);
        }
    }
}
