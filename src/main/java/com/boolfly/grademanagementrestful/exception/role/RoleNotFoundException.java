package com.boolfly.grademanagementrestful.exception.role;

import com.boolfly.grademanagementrestful.exception.generic.NotFoundException;

public class RoleNotFoundException extends NotFoundException {
    public RoleNotFoundException(String name) {
        super("Role " + name);
    }
}
