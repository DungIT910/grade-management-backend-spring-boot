package com.boolfly.grademanagementrestful.exception.generic;

import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;

public class NotFoundException extends GradeManagementRuntimeException {
    public NotFoundException(String message) {
        super(message + " not found");
    }
}
