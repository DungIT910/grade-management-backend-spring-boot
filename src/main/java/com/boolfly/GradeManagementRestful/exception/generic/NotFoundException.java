package com.boolfly.GradeManagementRestful.exception.generic;

import com.boolfly.GradeManagementRestful.exception.base.GradeManagementRuntimeException;

public class NotFoundException extends GradeManagementRuntimeException {
    public NotFoundException(String message) {
        super(message + " not found");
    }
}
