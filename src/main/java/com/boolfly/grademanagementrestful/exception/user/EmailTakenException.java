package com.boolfly.grademanagementrestful.exception.user;

import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;

public class EmailTakenException extends GradeManagementRuntimeException {
    public EmailTakenException(String email) {
        super("Email " + email + " is already taken");
    }
}
