package com.boolfly.grademanagementrestful.domain.model.role;

import lombok.Getter;

/**
 * Enum representing different roles in the system.
 * Each role has a name and a type associated with it.
 *
 * @author Bao Le
 * @see Getter
 */
@Getter
public enum RoleModel {
    /**
     * Role for students.
     */
    ROLE_STUDENT("ROLE_STUDENT", "student"),

    /**
     * Role for lecturers.
     */
    ROLE_LECTURER("ROLE_LECTURER", "lecturer");

    private final String roleName;
    private final String roleType;

    /**
     * Constructs a {@code RoleModel} with the specified role name and role type.
     *
     * @param roleName the name of the role
     * @param roleType the type of the role
     */
    RoleModel(String roleName, String roleType) {
        this.roleName = roleName;
        this.roleType = roleType;
    }
}
