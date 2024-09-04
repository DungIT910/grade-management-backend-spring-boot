package com.boolfly.grademanagementrestful.exception.user;

import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;

public class FailedAuthenticationException extends GradeManagementRuntimeException {
    public FailedAuthenticationException() {
        super("Authentication failed");
    }
}
