package com.boolfly.GradeManagementRestful.exception.user;

import com.boolfly.GradeManagementRestful.exception.base.GradeManagementRuntimeException;

public class IsNotStudentException extends GradeManagementRuntimeException {
    public IsNotStudentException() {
        super("This is not a student");
    }
}
