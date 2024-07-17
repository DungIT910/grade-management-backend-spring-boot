package com.boolfly.grademanagementrestful.exception.user;

import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;

public class UserNotFoundException extends GradeManagementRuntimeException {
    public UserNotFoundException(String id) {
        super("User " + id + " not found");
    }
}
