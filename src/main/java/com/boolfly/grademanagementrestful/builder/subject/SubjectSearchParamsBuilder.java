package com.boolfly.grademanagementrestful.builder.subject;

import com.boolfly.grademanagementrestful.api.dto.subject.SearchSubjectRequest;
import com.boolfly.grademanagementrestful.builder.base.AbstractSearchParamsBuilder;
import com.boolfly.grademanagementrestful.builder.entitypathprovider.subject.SubjectEntityPathProvider;
import com.boolfly.grademanagementrestful.domain.model.subject.SubjectStatus;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class SubjectSearchParamsBuilder extends AbstractSearchParamsBuilder {
    private final String subjectId;
    private final String name;
    private final List<SubjectStatus> status;

    public SubjectSearchParamsBuilder(SubjectBuilder builder) {
        super(builder);
        this.subjectId = builder.subjectId;
        this.name = builder.name;
        this.status = builder.status;
    }

    public static SubjectSearchParamsBuilder from(int page, int size, SearchSubjectRequest request) {
        return new SubjectSearchParamsBuilder.SubjectBuilder()
                .withPage(page)
                .withSize(size)
                .withName(request.getName())
                .withSubjectId(request.getSubjectId())
                .withStatus(request.getStatus())
                .build();
    }

    @Override
    public Optional<BooleanExpression> getCommonCriteria() {
        return getCommonCriteria(SubjectEntityPathProvider.getInstance(), name, subjectId, status);
    }

    public static class SubjectBuilder extends AbstractBuilder<SubjectBuilder, SubjectSearchParamsBuilder> {
        private String subjectId;
        private String name;
        private List<SubjectStatus> status;

        public SubjectBuilder withSubjectId(String subjectId) {
            this.subjectId = subjectId;
            return this;
        }

        public SubjectBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public SubjectBuilder withStatus(List<SubjectStatus> status) {
            this.status = Objects.requireNonNullElseGet(status, List::of);
            return this;
        }

        @Override
        public SubjectSearchParamsBuilder build() {
            return new SubjectSearchParamsBuilder(this);
        }
    }
}
