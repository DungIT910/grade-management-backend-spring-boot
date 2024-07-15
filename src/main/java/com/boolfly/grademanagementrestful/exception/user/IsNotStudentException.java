package com.boolfly.grademanagementrestful.exception.user;

import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;

public class IsNotStudentException extends GradeManagementRuntimeException {
    public IsNotStudentException() {
        super("This is not a student");
    }
}
