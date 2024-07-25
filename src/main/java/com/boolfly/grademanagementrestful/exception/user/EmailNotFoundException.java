package com.boolfly.grademanagementrestful.exception.user;

import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;

public class EmailNotFoundException extends GradeManagementRuntimeException {
    public EmailNotFoundException(String email) {
        super("Email " + email + " not found");
    }
}
