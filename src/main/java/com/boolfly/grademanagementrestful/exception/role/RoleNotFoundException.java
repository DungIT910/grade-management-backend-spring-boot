package com.boolfly.grademanagementrestful.exception.role;

import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;

public class RoleNotFoundException extends GradeManagementRuntimeException {
    public RoleNotFoundException(String name) {
        super("Role " + name + " not found");
    }
}
