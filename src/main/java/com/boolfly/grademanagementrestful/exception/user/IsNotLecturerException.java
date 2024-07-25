package com.boolfly.grademanagementrestful.exception.user;

import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;

public class IsNotLecturerException extends GradeManagementRuntimeException {
    public IsNotLecturerException() {
        super("This is not a lecturer");
    }
}
