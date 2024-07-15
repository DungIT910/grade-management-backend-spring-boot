package com.boolfly.grademanagementrestful.exception.generic;

import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;

public class NotNullException extends GradeManagementRuntimeException {
    public NotNullException(String message) {
        super(message + " must not be null");
    }
}
