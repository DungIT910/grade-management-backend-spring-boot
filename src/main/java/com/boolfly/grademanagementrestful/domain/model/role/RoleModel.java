package com.boolfly.grademanagementrestful.domain.model.role;

import lombok.Getter;

@Getter
public enum RoleModel {
    ROLE_STUDENT("ROLE_STUDENT", "student"),
    ROLE_LECTURER("ROLE_LECTURER", "lecturer");

    private final String roleName;
    private final String roleType;

    RoleModel(String roleName, String roleType) {
        this.roleName = roleName;
        this.roleType = roleType;
    }
}
