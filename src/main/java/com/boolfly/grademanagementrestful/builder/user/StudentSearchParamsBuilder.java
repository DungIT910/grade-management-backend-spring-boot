package com.boolfly.grademanagementrestful.builder.user;

import com.boolfly.grademanagementrestful.api.dto.user.SearchUserRequest;
import com.boolfly.grademanagementrestful.builder.base.AbstractSearchParamsBuilder;
import com.boolfly.grademanagementrestful.domain.model.role.RoleModel;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.boolfly.grademanagementrestful.domain.QUser.user;

public final class StudentSearchParamsBuilder extends AbstractSearchParamsBuilder {
    private final List<RoleModel> roles;
    private final String firstName;
    private final String lastName;
    private final String studentId;

    private StudentSearchParamsBuilder(StudentBuilder builder) {
        super(builder);
        this.roles = builder.roles;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.studentId = builder.studentId;
    }

    public static StudentSearchParamsBuilder from(int page, int size, SearchUserRequest request) {
        return new StudentBuilder()
                .withPage(page)
                .withSize(size)
                .withRoles(request.getRoles())
                .withFirstName(request.getFirstName())
                .withLastName(request.getLastName())
                .withStudentId(request.getStudentId())
                .build();
    }

    @Override
    public Optional<BooleanExpression> getCommonCriteria() {
        Optional<BooleanExpression> fullNameCriteria = Stream.of(
                        Optional.ofNullable(firstName)
                                .map(str -> "%" + firstName + "%")
                                .map(user.firstName::likeIgnoreCase),
                        Optional.ofNullable(lastName)
                                .map(str -> "%" + lastName + "%")
                                .map(user.lastName::likeIgnoreCase),
                        Optional.ofNullable(studentId)
                                .map(this::toTSIDLong)
                                .map(user.id::eq)
                )
                .filter(Optional::isPresent).map(Optional::get)
                .reduce(BooleanExpression::and);

        return Stream.of(
                        Optional.of(roles)
                                .map(r -> r.stream()
                                        .map(RoleModel::getRoleName)
                                        .toList())
                                .map(user.role.name::in),
                        fullNameCriteria
                )
                .filter(Optional::isPresent).map(Optional::get)
                .reduce(BooleanExpression::and);
    }

    public static class StudentBuilder extends AbstractBuilder<StudentBuilder, StudentSearchParamsBuilder> {
        private List<RoleModel> roles;
        private String firstName;
        private String lastName;
        private String studentId;

        public StudentBuilder withRoles(List<RoleModel> roles) {
            this.roles = roles;
            return this;
        }

        public StudentBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public StudentBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public StudentBuilder withStudentId(String studentId) {
            this.studentId = studentId;
            return this;
        }

        @Override
        public StudentSearchParamsBuilder build() {
            return new StudentSearchParamsBuilder(this);
        }
    }
}
