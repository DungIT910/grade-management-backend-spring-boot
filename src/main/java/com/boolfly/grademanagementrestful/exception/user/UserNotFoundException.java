package com.boolfly.grademanagementrestful.exception.user;

import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;

public class UserNotFoundException extends GradeManagementRuntimeException {
    public UserNotFoundException() {
        super("User not found");
    }
}
