package com.boolfly.GradeManagementRestful.exception.role;

import com.boolfly.GradeManagementRestful.exception.generic.NotFoundException;

public class RoleNotFoundException extends NotFoundException {
    public RoleNotFoundException(String name) {
        super("Role " + name);
    }
}
