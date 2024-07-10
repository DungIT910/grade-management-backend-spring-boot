package com.boolfly.GradeManagementRestful.exception.generic;

import com.boolfly.GradeManagementRestful.exception.base.GradeManagementRuntimeException;

public class NotNullException extends GradeManagementRuntimeException {
    public NotNullException(String message) {
        super(message + " must not be null");
    }
}
