package com.boolfly.GradeManagementRestful.api.dto.user;

import com.boolfly.GradeManagementRestful.domain.model.role.RoleModel;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class SearchUserRequest {
    private List<RoleModel> roles;

    public List<RoleModel> getRoles() {
        return Objects.requireNonNullElseGet(roles, () -> List.of(RoleModel.ROLE_STUDENT));
    }
}
