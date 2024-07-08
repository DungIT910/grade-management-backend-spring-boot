package com.boolfly.GradeManagementRestful.exception.role;

import com.boolfly.GradeManagementRestful.exception.base.GradeManagementRuntimeException;

public class RoleNotFoundException extends GradeManagementRuntimeException {
    public RoleNotFoundException(String name) {
        super("Role " + name + " not found");
    }
}
